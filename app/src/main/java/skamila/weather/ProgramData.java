package skamila.weather;

import android.app.Activity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import skamila.weather.api.forecast_data.City;
import skamila.weather.api.forecast_data.Unit;

public class ProgramData {

    private static ProgramData instance = new ProgramData();
    private long updateTime;
    private List<City> cities;
    private City actualCity;
    private Unit unit;

    private ProgramData() {
        cities = new ArrayList<>();
        unit = Unit.CELSIUM;
    }

    public static String getURL(String cityName) {
        return "https://api.openweathermap.org/data/2.5/forecast?q=" + cityName + "&appid=3758dae42d40b6cc1140947ed034389f";
    }

    public static String getURL(String cityName, String country) {
        return "https://api.openweathermap.org/data/2.5/forecast?q=" + cityName + ", " + country + "&appid=3758dae42d40b6cc1140947ed034389f";
    }

    public void setActualCity(String cityName) {
        for (City city : cities) {
            if (city.getName().equals(cityName)) {
                actualCity = city;
            }
        }
    }

    public static ProgramData getInstance() {
        return instance;
    }

    public ProgramData loadProgramData(Activity activity) {
        FileManager fileManager = new FileManager(activity, "data");
        String s = fileManager.loadFromFile();
        if (!s.equals("")) {
            Gson g = new Gson();
            instance = g.fromJson(s, ProgramData.class);
            return instance;
        }
        return new ProgramData();
    }

    public void saveProgramData(Activity activity) {
        Gson g = new Gson();
        String s = g.toJson(this);
        FileManager fm = new FileManager(activity, "data");
        fm.saveToFile(s);
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
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

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public String getUnitSymbol() {
        if (unit == Unit.CELSIUM) {
            return "°";
        } else if (unit == Unit.FAHRENHEIT) {
            return "ºF";
        } else {
            return "K";
        }
    }

    public Unit getUnit() {
        return unit;
    }

}