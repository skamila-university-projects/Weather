package skamila.weather.api;

import android.content.Context;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;

import skamila.weather.api.current_weather.CurrentWeather;
import skamila.weather.api.forecast.Forecast;

public class ApiController {

    private Context context;
    private ApiClient client;
    private FileManager fileManager;

    public ApiController(Context context, ApiClient client, FileManager fileManager){
        this.context = context;
        this.client = client;
        this.fileManager = fileManager;
    }

    public String downloadCurrentWeather(String city, String country){

        String url = "https://api.openweathermap.org/data/2.5/weather?q="  + city + "," + country + "&appid=3758dae42d40b6cc1140947ed034389f";
        return client.getResponse(context, url, fileManager);

    }

    public String downloadForecast(String city, String country){

        String url = "https://api.openweathermap.org/data/2.5/forecast?q="  + city + "," + country + "&appid=3758dae42d40b6cc1140947ed034389f";
        return client.getResponse(context, url, fileManager);

    }

    public CurrentWeather convertCurrentWeatherToObject(String data){

        try {
            FileWriter fw=new FileWriter("");
            fw.write(data);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(data);
        Gson g = new Gson();
        return g.fromJson(data, CurrentWeather.class);

    }


    public Forecast convertForecastToObject(String data){

        try {
            FileWriter fw=new FileWriter("");
            fw.write(data);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(data);
        Gson g = new Gson();
        return g.fromJson(data, Forecast.class);

    }


}
