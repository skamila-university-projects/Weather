package skamila.weather;

import java.util.HashMap;
import java.util.Map;

import skamila.weather.api.forecast_data.City;
import skamila.weather.api.forecast_data.Forecast;

public class FavoriteCitiesForecast {

    private static FavoriteCitiesForecast favoriteCitiesForecast = new FavoriteCitiesForecast();
    private Map<Integer, Forecast> forecast;

    private FavoriteCitiesForecast() {
        forecast = new HashMap<>();
    }

    public static FavoriteCitiesForecast getInstance() {
        return favoriteCitiesForecast;
    }

    public Forecast getForecast(City city) {
        return forecast.get(city.getId());
    }

    public void add(City city, Forecast forecast) {
        this.forecast.put(city.getId(), forecast);
    }

    public void delete(City city) {
        forecast.remove(city.getId());
    }

}
