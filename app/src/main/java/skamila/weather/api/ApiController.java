package skamila.weather.api;

import android.content.Context;

import com.google.gson.Gson;

import skamila.weather.api.forecast.Forecast;

public class ApiController {

    private Context context;
    private WeatherDownloader client;
    private FileManager fileManager;

    public ApiController(Context context, WeatherDownloader client, FileManager fileManager) {

        this.context = context;
        this.client = client;
        this.fileManager = fileManager;

    }

    public static Forecast convertForecastToObject(String data) {

        Gson g = new Gson();
        return g.fromJson(data, Forecast.class);

    }

    public static String convertObjectToJson(Forecast data) {

        Gson g = new Gson();
        return g.toJson(data);

    }


}
