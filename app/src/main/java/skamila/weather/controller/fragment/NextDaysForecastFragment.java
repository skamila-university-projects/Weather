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

import java.util.Calendar;
import java.util.Date;

import skamila.weather.R;

import static skamila.weather.ForecastDataGetter.*;

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
        refreshData();
        return view;
    }

    public void refreshData() {

        int[] componentIconID = {R.id.icon1, R.id.icon2, R.id.icon3};
        int[] componentDayID = {R.id.day1, R.id.day2, R.id.day3};
        int[] componentForecastID = {R.id.forecast1, R.id.forecast2, R.id.forecast3};

        Date[] date = new Date[3];

        Date today = new Date();
        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < date.length; i++){
            calendar.setTime(today);
            calendar.add(Calendar.DAY_OF_MONTH, i + 1);
            date[i] = calendar.getTime();
        }

        for (int i = 0; i < 3; i++) {

            ImageView icon = view.findViewById(componentIconID[i]);
            TextView day = view.findViewById(componentDayID[i]);
            TextView temp = view.findViewById(componentForecastID[i]);

            icon.setImageResource(getIconID(date[i]));
            day.setText(convertDate(date[i]));
            temp.setText(getMinMaxTemp(date[i]));

        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


}
