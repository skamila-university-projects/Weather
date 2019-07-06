package skamila.weather.controller;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Date;

import skamila.weather.ApiClient;
import skamila.weather.R;
import skamila.weather.api.DownloadedData;
import skamila.weather.api.WeatherDownloader;
import skamila.weather.api.ApiController;
import skamila.weather.api.Converter;
import skamila.weather.api.FavoriteCitiesForecast;
import skamila.weather.api.FileManager;
import skamila.weather.api.ProgramData;
import skamila.weather.api.forecast.City;
import skamila.weather.api.forecast.Forecast;
import skamila.weather.controller.fragment.MoreInformationFragment;
import skamila.weather.controller.fragment.NextDaysForecastFragment;

import static skamila.weather.api.ApiController.convertObjectToJson;

public class WeatherMain extends AppCompatActivity {

    private FavoriteCitiesForecast forecastForCities;
    private ProgramData programData;
    final Fragment nextDaysForecastFragment = new NextDaysForecastFragment();
    final Fragment moreInformationFragment = new MoreInformationFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        forecastForCities = FavoriteCitiesForecast.getInstance();
        programData = ProgramData.getInstance();
        programData.loadProgramData(this);

        prepareFragments();
        loadOrDownloadForecast();
        displayData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Gson g = new Gson();
        String s = g.toJson(programData);
        FileManager fm = new FileManager(this, "data");
        fm.saveToFile(s);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.refresh) {

            refresh();
            Toast toast = Toast.makeText(this, "Data are been refreshed", Toast.LENGTH_LONG);
            toast.show();
            return true;

        } else if (item.getItemId() == R.id.settings) {

            startActivity(new Intent(this, SettingsActivity.class));
            return true;

        }

        return false;

    }

    private AlertDialog prepareNewLocationDialog(int index, TextView city) {

        final AlertDialog.Builder d = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_localization, null);
        d.setTitle("Lokalizacja");
        d.setMessage("Wprowadź miasto");
        d.setView(dialogView);

        Button ok = dialogView.findViewById(R.id.ok);
        EditText input = dialogView.findViewById(R.id.input);

        final AlertDialog alertDialog = d.create();

        ok.setOnClickListener(e -> {
            input.getText();
            DownloadedData downloadedData = ApiClient.sendRequest(ProgramData.getURL(input.getText().toString()));
            if (downloadedData.status == 200) {
                Forecast forecast = ApiController.convertForecastToObject(downloadedData.data);
                forecastForCities.add(forecast.getCity(), forecast);
                programData.addCity(forecast.getCity(), index);
                FileManager fileManager = new FileManager(this, forecast.getCity().getName());
                fileManager.saveToFile(convertObjectToJson(forecast));
                city.setText(forecast.getCity().getName());
                alertDialog.cancel();
            } else {
                Toast toast = Toast.makeText(this, "Incorrect name", Toast.LENGTH_LONG);
                toast.show();
            }
        });

        return alertDialog;
    }

    private AlertDialog prepareLocationDialog() {

        final AlertDialog.Builder d = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.localization, null);
        d.setTitle("Lokalizacja");
        d.setMessage("Wprowadź miasto");
        d.setView(dialogView);

        TextView city1 = dialogView.findViewById(R.id.city1);
        Button change1 = dialogView.findViewById(R.id.change1);
        TextView city2 = dialogView.findViewById(R.id.city2);
        Button change2 = dialogView.findViewById(R.id.change2);
        TextView city3 = dialogView.findViewById(R.id.city3);
        Button change3 = dialogView.findViewById(R.id.change3);
        TextView city4 = dialogView.findViewById(R.id.city4);
        Button change4 = dialogView.findViewById(R.id.change4);

        final AlertDialog alertDialog = d.create();

        city1.setText(programData.getCitiesList().get(0).getName());
        if (programData.getCitiesList().size() > 1)
            city2.setText(programData.getCitiesList().get(1).getName());
        if (programData.getCitiesList().size() > 2)
            city3.setText(programData.getCitiesList().get(2).getName());
        if (programData.getCitiesList().size() > 3)
            city4.setText(programData.getCitiesList().get(3).getName());

        change1.setOnClickListener(e -> {
            AlertDialog alertDialog2 = prepareNewLocationDialog(0, city1);
            alertDialog2.show();
        });

        change2.setOnClickListener(e -> {
            AlertDialog alertDialog2 = prepareNewLocationDialog(1, city2);
            alertDialog2.show();
        });

        change3.setOnClickListener(e -> {
            AlertDialog alertDialog2 = prepareNewLocationDialog(2, city3);
            alertDialog2.show();
        });

        change4.setOnClickListener(e -> {
            AlertDialog alertDialog2 = prepareNewLocationDialog(3, city4);
            alertDialog2.show();
        });

        return alertDialog;
    }

    private void loadOrDownloadForecast() {

        for (City city : programData.getCitiesList()) {
            final FileManager fileManager = new FileManager(this, city.getName());
            String data;
            if (programData.areDataActual()) {
                data = fileManager.loadFromFile();
                Forecast forecast = ApiController.convertForecastToObject(data);
                forecastForCities.add(forecast.getCity(), forecast);
            } else {
                if (isInternetConnection()) {
                    WeatherDownloader weatherDownloader = new WeatherDownloader(this, ProgramData.getURL(city.getName()));
                    weatherDownloader.execute();
                } else {
                    Toast.makeText(this, "No connection to the Internet. \n" +
                            "Data may be out of date.", Toast.LENGTH_LONG).show();
                    data = fileManager.loadFromFile();
                    Forecast forecast = ApiController.convertForecastToObject(data);
                    forecastForCities.add(forecast.getCity(), forecast);
                }
            }
        }

    }

    public void prepareForecast(String data) {
        Forecast forecast = ApiController.convertForecastToObject(data);
        forecastForCities.add(forecast.getCity(), forecast);
        programData.addCity(forecast.getCity(), 0);
        programData.setActualCity(forecast.getCity());
        displayData();
        ((NextDaysForecastFragment) nextDaysForecastFragment).refreshData();
        ((MoreInformationFragment) moreInformationFragment).refreshData();
        FileManager fileManager = new FileManager(this, forecast.getCity().getName());
        fileManager.saveToFile(convertObjectToJson(forecast));
        programData.setUpdateTime(new Date().getTime());
    }

    private boolean isInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void prepareFragments() {

        ViewPager viewPager = findViewById(R.id.viewPager);

        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                if (i == 0) {
                    return nextDaysForecastFragment;
                } else {
                    return moreInformationFragment;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };

        viewPager.setAdapter(pagerAdapter);

    }

    private void setLocalization() {

    }

    private void refresh() {

    }

    public int getIconId(String iconName) {
        if (iconName.equals("01d")) {
            return R.drawable._01d;
        } else if (iconName.equals("01n")) {
            return R.drawable._01n;
        } else if (iconName.equals("02d")) {
            return R.drawable._02d;
        } else if (iconName.equals("02n")) {
            return R.drawable._02n;
        } else if (iconName.equals("03d") || iconName.equals("03n")) {
            return R.drawable._03d;
        } else if (iconName.equals("04d") || iconName.equals("04n")) {
            return R.drawable._04d;
        } else if (iconName.equals("09d") || iconName.equals("09n")) {
            return R.drawable._09d;
        } else if (iconName.equals("10d")) {
            return R.drawable._10d;
        } else if (iconName.equals("10n")) {
            return R.drawable._10n;
        } else if (iconName.equals("11d") || iconName.equals("11n")) {
            return R.drawable._11d;
        } else if (iconName.equals("13d") || iconName.equals("13n")) {
            return R.drawable._13d;
        } else if (iconName.equals("50d") || iconName.equals("50n")) {
            return R.drawable._50d;
        } else {
            return R.drawable._01d;
        }
    }

    public FavoriteCitiesForecast getForecastForCities() {
        return forecastForCities;
    }

    public ProgramData getProgramData() {
        return programData;
    }

    public void displayData() {

        if (programData.getActualCity() != null) {

            TextView city = findViewById(R.id.city);
            ImageView todayIcon = findViewById(R.id.todayIcon);
            TextView description = findViewById(R.id.description);
            TextView actualTemp = findViewById(R.id.actualTemp);
            TextView temp = findViewById(R.id.temp);

            city.setText(programData.getActualCity().getName() + ", " + programData.getActualCity().getCountry());
            Forecast forecast = forecastForCities.getForecast(programData.getActualCity());
            temp.setText(Converter.toMinMaxTemp(
                    Converter.toGoodUnit(forecast.getList().get(0).getMain().getTemp_max(), programData.getUnit()),
                    Converter.toGoodUnit(forecast.getList().get(0).getMain().getTemp_min(), programData.getUnit()),
                    programData.getUnitSymbol()
            ));

            description.setText(forecast.getList().get(0).getWeather().get(0).getDescription());

            todayIcon.setImageResource(getIconId(forecast.getList().get(0).getWeather().get(0).getIcon()));
        }

    }

}
