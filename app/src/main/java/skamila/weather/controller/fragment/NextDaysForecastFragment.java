package skamila.weather.controller.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import skamila.weather.R;
import skamila.weather.FavoriteCitiesForecast;
import skamila.weather.ProgramData;
import skamila.weather.api.forecast_data.Forecast;
import skamila.weather.api.forecast_data.Weather;

public class NextDaysForecastFragment extends Fragment {

    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.forecast, container, false);
        return view;
    }

    public void refreshData() {

        FavoriteCitiesForecast favoriteCitiesForecast = FavoriteCitiesForecast.getInstance();
        ProgramData programData = ProgramData.getInstance();
        Forecast forecast = favoriteCitiesForecast.getForecast(programData.getActualCity());

        int[] componentIconID = {R.id.icon1, R.id.icon2, R.id.icon3};
        int[] componentDayID = {R.id.day1, R.id.day2, R.id.day3};
        int[] componentForecastID = {R.id.forecast1, R.id.forecast2, R.id.forecast3};

        int forecastIndex = 2;

        for (int i = 0; i < 3; i++) {
            Weather weather = forecast.getWeathers().get(forecastIndex);
            TextView day = view.findViewById(componentDayID[i]);
            day.setText(weather.getDt_txt().substring(0, 10));
            TextView forecastPerDay = view.findViewById(componentForecastID[i]);
            //forecastPerDay.setText(toMinMaxTemp(toGoodUnit(weather.getMain().getTemp_max(), programData.getUnit()), toGoodUnit(weather.getMain().getTemp_min(), programData.getUnit()), programData.getUnitSymbol()));
            //int iconID = ((WeatherMain) getActivity()).getIconId(forecast.getWeathers().get(0).getWeatherDescriptions().get(0).getIcon());
            ImageView icon = view.findViewById(componentIconID[i]);
            //icon.setImageResource(iconID);
            forecastIndex += 8;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


}
