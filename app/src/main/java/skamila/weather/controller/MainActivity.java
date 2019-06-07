package skamila.weather.controller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import skamila.weather.R;
import skamila.weather.api.ApiClient;
import skamila.weather.api.ApiController;
import skamila.weather.api.FavoriteCitiesForecast;
import skamila.weather.api.FileManager;
import skamila.weather.api.ProgramData;
import skamila.weather.api.forecast.Forecast;

public class MainActivity extends AppCompatActivity {

    FavoriteCitiesForecast forecastForCities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        prepareForecast();

    }

    private void prepareForecast() {
        final FileManager fileManager = new FileManager(this, "data.txt");
        String data;
        forecastForCities = new FavoriteCitiesForecast();
        ProgramData programData = new ProgramData();

        if (programData.areDataActual()) {
            data = fileManager.loadFromFile();
        } else {
            if (isInternetConnection()) {
                ApiClient apiClient = new ApiClient();
                ApiController apiController = new ApiController(this, apiClient, fileManager);
                data = apiController.downloadForecast("Poznan", "pl");
            } else {
                Toast.makeText(this, "No connection to the Internet. \n" +
                        "Data may be out of date.", Toast.LENGTH_LONG).show();
                data = fileManager.loadFromFile();
            }
        }

        Forecast forecast = ApiController.convertForecastToObject(data);
        //TODO rozwiązać problem z danymi, które długo się pobierają
        forecastForCities.add(forecast.getCity(), forecast);

    }

    private boolean isInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


}
