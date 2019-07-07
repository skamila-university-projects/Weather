package skamila.weather;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import skamila.weather.api.forecast_data.Forecast;
import skamila.weather.api.forecast_data.Weather;

public class ForecastDataGetter {

    public static String getCity() {
        ProgramData programData = ProgramData.getInstance();
        return programData.getActualCity().getName() + ", " + programData.getActualCity().getCountry();
    }

    public static String getActualTemp() {
        ProgramData programData = ProgramData.getInstance();
        Weather weather = getWeather(new Date());
        return convertTemp(weather.getMain().getTemp()) + programData.getUnitSymbol();
    }

    public static String getTodayDescription() {
        Weather weather = getWeather(new Date());
        return weather.getWeatherDescriptions().get(0).getDescription();
    }

    public static String getMinMaxTemp(Date date) {
        ProgramData programData = ProgramData.getInstance();
        return convertTemp(getDayMinTemp(date)) + programData.getUnitSymbol() + " / "
                + convertTemp(getDayMaxTemp(date)) + programData.getUnitSymbol();
    }

    public static String getTodayClouds() {
        ProgramData programData = ProgramData.getInstance();
        Weather weather = getWeather(new Date());
        return String.valueOf(weather.getClouds().getAll());
    }

    public static String getTodayWindSpeed() {
        ProgramData programData = ProgramData.getInstance();
        Weather weather = getWeather(new Date());
        return String.valueOf(weather.getWind().getSpeed());
    }

    public static String getTodayWindDeg() {
        ProgramData programData = ProgramData.getInstance();
        Weather weather = getWeather(new Date());
        return String.valueOf(weather.getWind().getDeg());
    }

    public static int getIconID(Date date) {
        Weather weather = getWeather(date);
        weather.getWeatherDescriptions().get(0).getIcon();
        return convertToIconID(weather.getWeatherDescriptions().get(0).getIcon());
    }

    private static Weather getWeather(Date expectedDate) {

        FavoriteCitiesForecast favoriteCitiesForecast = FavoriteCitiesForecast.getInstance();
        ProgramData programData = ProgramData.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Forecast forecast = favoriteCitiesForecast.getForecast(programData.getActualCity());

        try {
            for (int i = 1; i < forecast.getWeathers().size(); i++) {
                Date nextDate = dateFormat.parse(forecast.getWeathers().get(i).getDt_txt());
                if (nextDate.before(expectedDate)) {
                    return forecast.getWeathers().get(i - 1);
                } else if (i == forecast.getWeathers().size() - 1) {
                    return forecast.getWeathers().get(i);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static double getDayMinTemp(Date date) {

        FavoriteCitiesForecast favoriteCitiesForecast = FavoriteCitiesForecast.getInstance();
        ProgramData programData = ProgramData.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Forecast forecast = favoriteCitiesForecast.getForecast(programData.getActualCity());
        double min = forecast.getWeathers().get(0).getMain().getTemp();

        try {
            for (int i = 1; i < forecast.getWeathers().size(); i++) {
                Date nextDate = dateFormat.parse(forecast.getWeathers().get(i).getDt_txt());
                if (nextDate.after(date) && nextDate.before(date)) {
                    //TODO manipulacja datami
                    if (min > forecast.getWeathers().get(i).getMain().getTemp()) {
                        min = forecast.getWeathers().get(i).getMain().getTemp();
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return min;

    }

    private static double getDayMaxTemp(Date date) {

        FavoriteCitiesForecast favoriteCitiesForecast = FavoriteCitiesForecast.getInstance();
        ProgramData programData = ProgramData.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Forecast forecast = favoriteCitiesForecast.getForecast(programData.getActualCity());
        double max = forecast.getWeathers().get(0).getMain().getTemp();

        try {
            for (int i = 1; i < forecast.getWeathers().size(); i++) {
                Date nextDate = dateFormat.parse(forecast.getWeathers().get(i).getDt_txt());
                //TODO manipulacja datami
                if (nextDate.after(date) && nextDate.before(date)) {
                    if (max < forecast.getWeathers().get(i).getMain().getTemp()) {
                        max = forecast.getWeathers().get(i).getMain().getTemp();
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return max;

    }

    private static String convertTemp(double temp) {
        Unit unit = ProgramData.getInstance().getUnit();
        if (unit == Unit.CELSIUM) {
            return String.format(Locale.getDefault(), "%.1f", temp - 273.15);
        } else if (unit == Unit.FAHRENHEIT) {
            return String.format(Locale.getDefault(), "%.1f", (9.0 / 5.0) * (temp - 273) + 32);
        } else {
            return String.format(Locale.getDefault(), "%.1f", temp);
        }
    }

    private static int convertToIconID(String iconName) {
        if (iconName.equals("01d")) {
            return R.drawable._01d;
        } else if (iconName.equals("01n")) {
            return R.drawable._01n;
        } else if (iconName.equals("02d")) {
            return R.drawable._02d;
        } else if (iconName.equals("02n")) {
            return R.drawable._02n;
        } else if (iconName.equals("03d") || iconName.equals("03n")) {
            return R.drawable._03d;
        } else if (iconName.equals("04d") || iconName.equals("04n")) {
            return R.drawable._04d;
        } else if (iconName.equals("09d") || iconName.equals("09n")) {
            return R.drawable._09d;
        } else if (iconName.equals("10d")) {
            return R.drawable._10d;
        } else if (iconName.equals("10n")) {
            return R.drawable._10n;
        } else if (iconName.equals("11d") || iconName.equals("11n")) {
            return R.drawable._11d;
        } else if (iconName.equals("13d") || iconName.equals("13n")) {
            return R.drawable._13d;
        } else if (iconName.equals("50d") || iconName.equals("50n")) {
            return R.drawable._50d;
        } else {
            return R.drawable._01d;
        }
    }

}
