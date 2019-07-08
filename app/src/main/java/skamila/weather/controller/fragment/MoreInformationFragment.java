package skamila.weather.controller.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import skamila.weather.ProgramData;
import skamila.weather.R;

import static skamila.weather.ForecastDataGetter.*;

public class MoreInformationFragment extends Fragment {

    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.more_information, container, false);
        if(ProgramData.getInstance().getActualCity() != null){
            refreshData();
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void refreshData() {

        int[] namesID = {R.id.name1, R.id.name2, R.id.name3, R.id.name4};
        int[] dataID = {R.id.data1, R.id.data2, R.id.data3, R.id.data4};
        String[] information = {getTodayPressure(), getTodayClouds(), getTodayWindSpeed(), getTodayWindDeg()};
        String[] names = {"Pressure", "Clouds", "Wind Speed", "Wind directions"};

        for (int i = 0; i < 4; i++) {
            TextView name = view.findViewById(namesID[i]);
            TextView data = view.findViewById(dataID[i]);
            name.setText(names[i]);
            data.setText(information[i]);
        }

    }

}
