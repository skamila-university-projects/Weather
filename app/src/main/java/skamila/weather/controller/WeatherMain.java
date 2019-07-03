package skamila.weather.controller;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import skamila.weather.R;
import skamila.weather.api.ApiClient;
import skamila.weather.api.ApiController;
import skamila.weather.api.Converter;
import skamila.weather.api.FavoriteCitiesForecast;
import skamila.weather.api.FileManager;
import skamila.weather.api.ProgramData;
import skamila.weather.api.forecast.Forecast;
import skamila.weather.api.forecast.Weather;
import skamila.weather.api.forecast.WeatherDescription;
import skamila.weather.controller.fragment.MoreInformationFragment;
import skamila.weather.controller.fragment.NextDaysForecastFragment;

public class WeatherMain extends AppCompatActivity {

    private FavoriteCitiesForecast forecastForCities;
    private ProgramData programData;
    final Fragment nextDaysForecastFragment = new NextDaysForecastFragment();
    final Fragment moreInformationFragment = new MoreInformationFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        forecastForCities = ViewModelProviders.of(this).get(FavoriteCitiesForecast.class);
        programData = ViewModelProviders.of(this).get(ProgramData.class);

        prepareForecast();
        //displayData();
        prepareFragments();
        prepareButtons();
        prepareClickedComponents();

    }

    private void prepareClickedComponents() {
        ImageView refresh = findViewById(R.id.refresh);
        ImageView localization = findViewById(R.id.localization);
        localization.setOnClickListener(e -> {
            AlertDialog alertDialog = prepareLocationDialog();
            alertDialog.show();
        });
    }

    private AlertDialog prepareLocationDialog(){

        final AlertDialog.Builder d = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.localization, null);
        d.setTitle("Lokalizacja");
        d.setMessage("Wprowadź miasto");
        d.setView(dialogView);

        final AlertDialog alertDialog = d.create();

        return alertDialog;
    }

    private void prepareForecast() {
        final FileManager fileManager = new FileManager(this, "data.txt");
        String data;
        String url = "https://api.openweathermap.org/data/2.5/forecast?q="  + "Poznan" + "," + "pl" + "&appid=3758dae42d40b6cc1140947ed034389f";

//        if (programData.areDataActual()) {
        data = fileManager.loadFromFile();
//        } else {
//            if (isInternetConnection()) {
                ApiClient apiClient = new ApiClient(this, url);
                apiClient.execute();
                //ApiController apiController = new ApiController(this, apiClient, fileManager);
                //apiController.downloadForecast("Poznan", "pl", this);
                //data = apiController.downloadForecast("Poznan", "pl");
//            } else {
//                Toast.makeText(this, "No connection to the Internet. \n" +
//                        "Data may be out of date.", Toast.LENGTH_LONG).show();
//                data = fileManager.loadFromFile();
//            }
//        }

//        fillBasicInformation();

    }

    public void prepareForecast(String data) {
        Forecast forecast = ApiController.convertForecastToObject(data);
        forecastForCities.add(forecast.getCity(), forecast);
        programData.setActualCity(forecast.getCity());
        fillBasicInformation();
        ((NextDaysForecastFragment) nextDaysForecastFragment).refreshData();
        ((MoreInformationFragment) moreInformationFragment).refreshData();
    }

    private boolean isInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void fillBasicInformation() {
        TextView cityTextView = findViewById(R.id.city);
        TextView actualTempTextView = findViewById(R.id.actualTemp);
        TextView tempTextView = findViewById(R.id.temp);
        TextView descriptionTextView = findViewById(R.id.description);
        ImageView todayIcon = findViewById(R.id.todayIcon);

        cityTextView.setText(programData.getActualCity().getName() + ", "
                + programData.getActualCity().getCountry());

        List<Weather> weatherList = forecastForCities.getForecast(programData.getActualCity()).getList();

        double actualTemp = Math.round(weatherList.get(0).getMain().getTemp() - 273.15);
        double tempMax = Math.round(weatherList.get(0).getMain().getTemp_max() - 273.15);
        double tempMin = Math.round(weatherList.get(0).getMain().getTemp_min() - 273.15);

        actualTempTextView.setText(actualTemp + "°");
        tempTextView.setText(tempMax + "° / " + tempMin + "°");

        List<WeatherDescription> weatherDescriptions = weatherList.get(0).getWeather();

        descriptionTextView.setText(weatherDescriptions.get(0).getDescription());
        todayIcon.setImageResource(getIconId(weatherDescriptions.get(0).getIcon()));
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

    private void prepareButtons() {
        ImageView localization = findViewById(R.id.localization);
        localization.setOnClickListener(e -> {
            setLocalization();
        });
        ImageView refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(e -> {
            refresh();
        });
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

    public void displayData(){
        TextView city = findViewById(R.id.city);
        ImageView todayIcon = findViewById(R.id.todayIcon);
        TextView description = findViewById(R.id.description);
        TextView actualTemp = findViewById(R.id.actualTemp);
        TextView temp = findViewById(R.id.temp);

        city.setText(programData.getActualCity().getName() +", " + programData.getActualCity().getCountry());
        Forecast forecast = forecastForCities.getForecast(programData.getActualCity());
        temp.setText(Converter.toMinManString(
                Converter.toGoodUnit(forecast.getList().get(0).getMain().getTemp_max(), programData.getUnit()),
                Converter.toGoodUnit(forecast.getList().get(0).getMain().getTemp_min(), programData.getUnit()),
                programData.getUnitSymbol()
                ));
        description.setText(forecast.getList().get(0).getWeather().get(0).getDescription());

        todayIcon.setImageResource(getIconId(forecast.getList().get(0).getWeather().get(0).getIcon()));
    }

}
