package skamila.weather.api.forecast_data;

import java.util.List;

public class Forecast {

    private String cod;
    private double message;
    private int cnt;
    private City city;
    private List<Weather> list;

    public String getCod() {
        return cod;
    }

    public double getMessage() {
        return message;
    }

    public int getCnt() {
        return cnt;
    }

    public City getCity() {
        return city;
    }

    public List<Weather> getWeathers() {
        return list;
    }

}
