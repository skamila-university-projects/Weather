package skamila.weather.api;

import java.util.Map;

import skamila.weather.api.forecast.City;
import skamila.weather.api.forecast.Forecast;

public class FavoriteCitiesForecast {

    private Map<City, Forecast> forecast;

    public Forecast getForecast(City city){
        return forecast.get(city);
    }

    public void addCity(City city, Forecast forecast){
        this.forecast.put(city, forecast);
    }

    public void deleteCity(City city){
        forecast.remove(city);
    }

}
