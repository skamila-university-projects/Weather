package skamila.weather.controller.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import skamila.weather.R;
import skamila.weather.controller.fragment.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty);
        getFragmentManager().beginTransaction().add(R.id.container, new SettingsFragment()).commit();

    }

}
