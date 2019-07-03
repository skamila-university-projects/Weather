package skamila.weather.api;

import java.util.Locale;

import skamila.weather.api.forecast.Unit;

public class Converter {

    public static double toGoodUnit(double temp, Unit unit){
        if(unit == Unit.CELSIUM){
            return Double.parseDouble(String.format(Locale.getDefault(), "%.2f", temp - 273.15));
        } else {
            return Double.parseDouble(String.format(Locale.getDefault(), "%.2f", temp));
        }
    }

    public static String toMinManString(double tempMin, double tempMax, char symbol) {
        return tempMax + "" + symbol + " / " + tempMin + "" + symbol;
    }

}
