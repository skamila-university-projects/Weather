package skamila.weather.controller.fragment;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;

import java.util.ArrayList;
import java.util.List;

import skamila.weather.R;
import skamila.weather.api.ProgramData;
import skamila.weather.api.forecast.City;
import skamila.weather.api.forecast.Unit;

public class SettingsFragment extends PreferenceFragment {

    List<String> cities = new ArrayList<>();

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

        Unit unit = ProgramData.getInstance().getUnit();
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
            ProgramData programData = ProgramData.getInstance();
            if (b.equals("Celsium")) {
                programData.setUnit(Unit.CELSIUM);
            } else if (b.equals("Fahrenheit")) {
                programData.setUnit(Unit.FAHRENHEIT);
            } else if (b.equals("Kelvin")) {
                programData.setUnit(Unit.KELVIN);
            }
            return true;
        });

    }

    private void prepareNewCity() {

        EditTextPreference newCity = (EditTextPreference) findPreference("newCity");

    }

    private void prepareCityList() {

        ListPreference cityList = (ListPreference) findPreference("cityList");

        List<String> cities = new ArrayList<>();
        ProgramData programData = ProgramData.getInstance();

        for (City city : programData.getCitiesList()) {
            cities.add(city.getName() + ", " + city.getCountry());
        }

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
