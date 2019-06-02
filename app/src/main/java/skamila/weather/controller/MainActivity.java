package skamila.weather.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import skamila.weather.R;
import skamila.weather.api.ApiClient;
import skamila.weather.api.ApiController;
import skamila.weather.api.FileManager;
import skamila.weather.api.forecast.Forecast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView = findViewById(R.id.text);

        final FileManager fileManager = new FileManager(this, "data.txt");

        ApiClient apiClient = new ApiClient();
        ApiController apiController = new ApiController(this, apiClient, fileManager);
        String data = apiController.downloadForecast("Pozznan", "pl");


        textView.setText(fileManager.loadFromFile());
        Forecast forecast = apiController.convertForecastToObject(fileManager.loadFromFile());

    }

}
