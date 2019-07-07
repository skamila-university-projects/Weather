package skamila.weather.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import skamila.weather.R;
import skamila.weather.api.connection.WeatherDownloader;
import skamila.weather.api.connection.ApiController;
import skamila.weather.Converter;
import skamila.weather.FavoriteCitiesForecast;
import skamila.weather.FileManager;
import skamila.weather.ProgramData;
import skamila.weather.api.forecast_data.City;
import skamila.weather.api.forecast_data.Forecast;
import skamila.weather.controller.fragment.MoreInformationFragment;
import skamila.weather.controller.fragment.NextDaysForecastFragment;

import static skamila.weather.api.connection.ApiController.convertObjectToJson;

public class WeatherMain extends AppCompatActivity {

    private FavoriteCitiesForecast forecastForCities = FavoriteCitiesForecast.getInstance();
    private ProgramData programData = ProgramData.getInstance();
    final Fragment nextDaysForecastFragment = new NextDaysForecastFragment();
    final Fragment moreInformationFragment = new MoreInformationFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        programData = programData.loadProgramData(this);

        prepareFragments();
        loadOrDownloadForecast();
        displayData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        programData.saveProgramData(this);
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