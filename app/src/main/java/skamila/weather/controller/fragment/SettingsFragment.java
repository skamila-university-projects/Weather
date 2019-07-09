package skamila.weather.controller.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import skamila.weather.api.connection.ApiClient;
import skamila.weather.R;
import skamila.weather.api.connection.ApiController;
import skamila.weather.api.connection.DownloadedData;
import skamila.weather.FavoriteCitiesForecast;
import skamila.weather.FileManager;
import skamila.weather.ProgramData;
import skamila.weather.api.forecast_data.City;
import skamila.weather.api.forecast_data.Forecast;
import skamila.weather.Unit;
import skamila.weather.database.Cities;

import static skamila.weather.api.connection.ApiController.convertObjectToJson;

public class SettingsFragment extends PreferenceFragment {

    private ListPreference cityList;
    private ProgramData programData = ProgramData.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        prepareNewCity();
        prepareCityList();
        prepareUnitsList();

    }

    private void prepareUnitsList() {

        ListPreference unitsList = (ListPreference) findPreference("unitsList");

        List<String> units = new ArrayList<>();
        units.add("Celsium");
        units.add("Fahrenheit");
        units.add("Kelvin");

        Unit unit = programData.getUnit();
        if (unit == Unit.CELSIUM) {
            unitsList.setValue("Celsium");
        } else if (unit == Unit.FAHRENHEIT) {
            unitsList.setValue("Fahrenheit");
        } else {
            unitsList.setValue("Kelvin");
        }

        CharSequence[] entries = units.toArray(new CharSequence[0]);
        CharSequence[] entryValues = units.toArray(new CharSequence[0]);
        unitsList.setEntries(entries);
        unitsList.setEntryValues(entryValues);

        unitsList.setOnPreferenceChangeListener((a, b) -> {
            if (b.equals("Celsium")) {
                programData.setUnit(Unit.CELSIUM);
            } else if (b.equals("Fahrenheit")) {
                programData.setUnit(Unit.FAHRENHEIT);
            } else if (b.equals("Kelvin")) {
                programData.setUnit(Unit.KELVIN);
            }
            programData.saveProgramData(this.getActivity());
            return true;
        });

    }

    private void prepareNewCity() {

        EditTextPreference newCity = (EditTextPreference) findPreference("newCity");
        newCity.setOnPreferenceChangeListener((a, b) -> {
            DownloadedData downloadedData = ApiClient.sendRequest(ProgramData.getURL(b.toString()));

            if (downloadedData.status == 200) {

                Forecast forecast = ApiController.convertForecastToObject(downloadedData.data);
                FavoriteCitiesForecast favoriteCitiesForecast = FavoriteCitiesForecast.getInstance();
                favoriteCitiesForecast.add(forecast.getCity(), forecast);

                Cities citiesSQL = new Cities(this.getContext());
                SQLiteDatabase database = citiesSQL.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                String city = forecast.getCity().getName() + ", " + forecast.getCity().getCountry();

                if(!programData.isCity(city)){
                    programData.addCity(forecast.getCity());
                    contentValues.put("name", city);
                    database.insert("City", null, contentValues);

                    FileManager fileManager = new FileManager(this.getContext(), forecast.getCity().getName());
                    fileManager.saveToFile(convertObjectToJson(forecast));
                    if (programData.getActualCity() == null) {
                        programData.setActualCity(programData.getCitiesList().get(0));
                    }
                    programData.saveProgramData(this.getActivity());
                }

            } else {
                Toast toast = Toast.makeText(this.getContext(), "Incorrect name", Toast.LENGTH_LONG);
                toast.show();
            }

            updateCityList();
            return true;
        });

    }

    private void prepareCityList() {

        cityList = (ListPreference) findPreference("cityList");
        updateCityList();

        cityList.setOnPreferenceChangeListener((a, b) -> {
            String[] chosenCity = ((String) b).split(", ");
            programData.setActualCity(chosenCity[0]);
            return true;
        });

    }

    private void updateCityList() {

        List<String> cities = new ArrayList<>();

        Cities citiesSQL = new Cities(this.getContext());
        SQLiteDatabase database = citiesSQL.getWritableDatabase();
        String query = "SELECT * FROM City";
        Cursor cursor = database.rawQuery(query, null);

        while(cursor.moveToNext()) {
            cities.add(cursor.getString(1));
        }

        cursor.close();

//        for (City city : programData.getCitiesList()) {
//            cities.add(city.getName() + ", " + city.getCountry());
//        }

        CharSequence[] entries = cities.toArray(new CharSequence[0]);
        CharSequence[] entryValues = cities.toArray(new CharSequence[0]);
        cityList.setEntries(entries);
        cityList.setEntryValues(entryValues);

    }

    @Override
    public void onDestroy() {

        super.onDestroy();

    }

}
