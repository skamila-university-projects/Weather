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

import skamila.weather.ForecastDataGetter;
import skamila.weather.R;
import skamila.weather.api.connection.WeatherDownloader;
import skamila.weather.api.connection.ApiController;
import skamila.weather.FavoriteCitiesForecast;
import skamila.weather.FileManager;
import skamila.weather.ProgramData;
import skamila.weather.api.forecast_data.City;
import skamila.weather.api.forecast_data.Forecast;
import skamila.weather.controller.fragment.MoreInformationFragment;
import skamila.weather.controller.fragment.NextDaysForecastFragment;

import static skamila.weather.ForecastDataGetter.*;
import static skamila.weather.api.connection.ApiController.convertObjectToJson;

public class WeatherMain extends AppCompatActivity {

    private FavoriteCitiesForecast forecastForCities = FavoriteCitiesForecast.getInstance();
    private ProgramData programData = ProgramData.getInstance();
    private final Fragment nextDaysForecastFragment = new NextDaysForecastFragment();
    private final Fragment moreInformationFragment = new MoreInformationFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        programData = programData.loadProgramData(this);

        prepareFragments();
        loadOrDownloadForecast();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        refresh();
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
            if(programData.getActualCity() != null){
                refresh();
                Toast.makeText(this, "Data are been refreshed", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "You have to add city first", Toast.LENGTH_LONG).show();
            }
            return true;
        } else if (item.getItemId() == R.id.settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return false;
    }

    public void refresh() {
        try{
            if (programData.getActualCity() != null){
                prepareFragments();
                refreshBasicInformation();
                ((NextDaysForecastFragment) nextDaysForecastFragment).refreshData();
                ((MoreInformationFragment) moreInformationFragment).refreshData();
            }
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public void prepareForecast(String data) {
        Forecast forecast = ApiController.convertForecastToObject(data);
        forecastForCities.add(forecast.getCity(), forecast);
        programData.addCity(forecast.getCity(), 0);
        programData.setActualCity(forecast.getCity());
        refresh();
        FileManager fileManager = new FileManager(this, forecast.getCity().getName());
        fileManager.saveToFile(convertObjectToJson(forecast));
        programData.setUpdateTime(new Date().getTime());
    }

    private void loadOrDownloadForecast() {

        for (City city : programData.getCitiesList()) {
            final FileManager fileManager = new FileManager(this, city.getName());
            String data;
            if (programData.areDataActual()) {
                data = fileManager.loadFromFile();
                Forecast forecast = ApiController.convertForecastToObject(data);
                forecastForCities.add(forecast.getCity(), forecast);
                refresh();
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
                    refresh();
                }
            }
        }

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

    private void refreshBasicInformation() {

        if (programData.getActualCity() != null) {

            TextView city = findViewById(R.id.city);
            TextView actualTemp = findViewById(R.id.actualTemp);
            TextView temp = findViewById(R.id.temp);
            TextView description = findViewById(R.id.description);
            ImageView todayIcon = findViewById(R.id.todayIcon);

            city.setText(getCity());
            actualTemp.setText(getActualTemp());
            temp.setText(getMinMaxTemp(new Date()));
            description.setText(getTodayDescription());
            todayIcon.setImageResource(ForecastDataGetter.getIconID(new Date()));

        }

    }

}
