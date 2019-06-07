package skamila.weather.controller.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import skamila.weather.R;

public class NextDaysForecastFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.forecast, container, false);

        ImageView icon1 = view.findViewById(R.id.icon1);
        icon1.setImageResource(R.drawable._02d);

        ImageView icon2 = view.findViewById(R.id.icon2);
        icon2.setImageResource(R.drawable._09d);

        ImageView icon3 = view.findViewById(R.id.icon3);
        icon3.setImageResource(R.drawable._13d);

        return view;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }

}
