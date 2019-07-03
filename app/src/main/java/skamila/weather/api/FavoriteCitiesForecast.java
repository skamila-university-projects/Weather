package skamila.weather.api;

import android.arch.lifecycle.ViewModel;

import java.util.HashMap;
import java.util.Map;

import skamila.weather.api.forecast.City;
import skamila.weather.api.forecast.Forecast;

public class FavoriteCitiesForecast extends ViewModel {

    private Map<City, Forecast> forecast;

    public FavoriteCitiesForecast(){
        forecast = new HashMap<>();
    }

    public Forecast getForecast(City city){
        return forecast.get(city);
    }

    public void add(City city, Forecast forecast){
        this.forecast.put(city, forecast);
    }

    public void delete(City city){
        forecast.remove(city);
    }

}
