package skamila.weather.api.forecast_data;

import java.util.List;

public class Weather {

    private String dt;
    private Main main;
    private List<WeatherDescription> weather;
    private Clouds clouds;
    private Wind wind;
    private Sys sys;
    private String dt_txt;

    public String getDt() {
        return dt;
    }

    public Main getMain() {
        return main;
    }

    public List<WeatherDescription> getWeather() {
        return weather;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public Wind getWind() {
        return wind;
    }

    public Sys getSys() {
        return sys;
    }

    public String getDt_txt() {
        return dt_txt;
    }

}
