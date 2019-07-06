package skamila.weather.api;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import skamila.weather.api.forecast.City;
import skamila.weather.api.forecast.Unit;

public class ProgramData extends ViewModel {

    private long updateTime;
    private List<City> cities;
    private City actualCity;
    private Unit unit;

    public ProgramData() {
        cities = new ArrayList<>();
        unit = Unit.CELSIUM;
    }

    public static ProgramData loadProgramData(Activity activity){
        FileManager fileManager = new FileManager(activity, "data");
        String s = fileManager.loadFromFile();
        if(!s.equals("")){
            Gson g = new Gson();
            return g.fromJson(s, ProgramData.class);
        } else {
            return new ProgramData();
        }
    }

    public void setUpdateTime(long updateTime){
        this.updateTime = updateTime;
    }

    public boolean areDataActual() {
        return true;
        //return new Date().getTime() - updateTime <= 10800;
    }

    public List<City> getCitiesList() {
        return cities;
    }

    public void addCity(City city) {
        cities.add(city);
    }

    public void addCity(City city, int index) {
        cities.add(index, city);
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

    public String getUnitSymbol(){
        if(unit == Unit.CELSIUM){
            return "°";
        } else if(unit == Unit.FAHRENHEIT) {
            return "ºF";
        } else {
            return "K";
        }
    }

    public Unit getUnit() {
        return unit;
    }

}
