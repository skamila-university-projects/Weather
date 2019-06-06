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
import skamila.weather.api.FileManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if(!isInternetConnection()){
            Toast.makeText(this, "No connection to the Internet. \n" +
                    "Data may be out of date.", Toast.LENGTH_LONG).show();
        }

        final FileManager fileManager = new FileManager(this, "data.txt");

        ApiClient apiClient = new ApiClient();
        ApiController apiController = new ApiController(this, apiClient, fileManager);
        String data = apiController.downloadForecast("Lodz", "pl");

    }

    private boolean isInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
