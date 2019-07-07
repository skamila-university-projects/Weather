package skamila.weather;

import java.util.Locale;

import skamila.weather.api.forecast_data.Unit;

public class Converter {

    public static double toGoodUnit(double temp, Unit unit) {
        if (unit == Unit.CELSIUM) {
            return Double.parseDouble(String.format(Locale.getDefault(), "%.1f", temp - 273.15));
        } else if (unit == Unit.FAHRENHEIT) {
            return Double.parseDouble(String.format(Locale.getDefault(), "%.1f", (9.0 / 5.0) * (temp - 273) + 32));
        } else {
            return Double.parseDouble(String.format(Locale.getDefault(), "%.1f", temp));
        }
    }

    public static String toMinMaxTemp(double tempMin, double tempMax, String symbol) {
        return tempMax + "" + symbol + " / " + tempMin + "" + symbol;
    }

}
