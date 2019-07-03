package skamila.weather.api;

import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import skamila.weather.api.forecast.City;
import skamila.weather.api.forecast.Unit;

public class ProgramData extends ViewModel {

    private String lastUpdateDate;
    private List<City> cities;
    private City actualCity;
    private Unit unit;

    public ProgramData() {
        cities = new ArrayList<>();
        unit = Unit.CELSIUM;
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

    public char getUnitSymbol(){
        if(unit == Unit.CELSIUM){
            return '°';
        } else {
            return 'K';
        }
    }

    public Unit getUnit() {
        return unit;
    }

}
