package skamila.weather.api;

import java.util.ArrayList;
import java.util.List;

import skamila.weather.api.forecast.City;

public class ProgramData {

    private String lastUpdateDate;
    private List<City> cities;
    private City actualCity;

    public ProgramData() {
        cities = new ArrayList<>();
    }

    public boolean areDataActual() {
        return true;
    }

    public List<City> getCitiesList() {
        return cities;
    }

    public void addCity(City city) {
        cities.add(city);
    }

    public void deleteCity(City city) {
        cities.remove(city);
    }

    public City getActualCity() {
        return actualCity;
    }

    public void setActualCity(City city) {
        actualCity = city;
    }

}
