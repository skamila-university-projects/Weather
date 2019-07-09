package skamila.weather;

import android.app.Activity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import skamila.weather.api.forecast_data.City;

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
                instance.actualCity = city;
            }
        }
    }

    public boolean isCity(String _city) {
        for (City city : instance.cities) {
            if ((city.getName() + ", " + city.getCountry()).equals(_city)){
                return true;
            }
        }
        return false;
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
        instance = new ProgramData();
        return instance;
    }

    public void saveProgramData(Activity activity) {
        Gson g = new Gson();
        String s = g.toJson(this);
        FileManager fm = new FileManager(activity, "data");
        fm.saveToFile(s);
    }

    public long getUpdateTime() {
        return instance.updateTime;
    }

    public void setUpdateTime(long updateTime) {
        instance.updateTime = updateTime;
    }

    public boolean areDataActual() {
        return false;
    }

    public List<City> getCitiesList() {
        return instance.cities;
    }

    public void addCity(City city) {
        instance.cities.add(city);
    }

    public void addCity(City city, int index) {
        instance.cities.add(index, city);
    }

    public void deleteCity(City city) {
        instance.cities.remove(city);
    }

    public City getActualCity() {
        return instance.actualCity;
    }

    public void setActualCity(City city) {
        instance.actualCity = city;
    }

    public void setUnit(Unit unit) {
        instance.unit = unit;
    }

    public String getUnitSymbol() {
        if (instance.unit == Unit.CELSIUM) {
            return "°";
        } else if (instance.unit == Unit.FAHRENHEIT) {
            return "ºF";
        } else {
            return "K";
        }
    }

    public Unit getUnit() {
        return instance.unit;
    }

}
