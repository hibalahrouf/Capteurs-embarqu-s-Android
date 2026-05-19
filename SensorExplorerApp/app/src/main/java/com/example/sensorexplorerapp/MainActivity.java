package com.example.sensorexplorerapp;

import android.hardware.Sensor;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.sensorexplorerapp.fragments.ActivityRecognitionFragment;
import com.example.sensorexplorerapp.fragments.CompassFragment;
import com.example.sensorexplorerapp.fragments.LiveSensorFragment;
import com.example.sensorexplorerapp.fragments.MotionSensorFragment;
import com.example.sensorexplorerapp.fragments.SensorsListFragment;
import com.example.sensorexplorerapp.fragments.StepCounterFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final List<Button> navigationButtons = new ArrayList<>();
    private TextView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleView = findViewById(R.id.title_view);
        buildNavigation();

        if (savedInstanceState == null) {
            showScreen(0, new SensorsListFragment(), getString(R.string.screen_sensors));
        }
    }

    private void buildNavigation() {
        LinearLayout tabStrip = findViewById(R.id.tab_strip);
        addTab(tabStrip, R.string.screen_sensors, () -> new SensorsListFragment());
        addTab(tabStrip, R.string.screen_temperature,
                () -> LiveSensorFragment.create(Sensor.TYPE_AMBIENT_TEMPERATURE, "Ambient temperature", "C"));
        addTab(tabStrip, R.string.screen_humidity,
                () -> LiveSensorFragment.create(Sensor.TYPE_RELATIVE_HUMIDITY, "Relative humidity", "%"));
        addTab(tabStrip, R.string.screen_proximity,
                () -> LiveSensorFragment.create(Sensor.TYPE_PROXIMITY, "Proximity", "cm"));
        addTab(tabStrip, R.string.screen_magnetic,
                () -> LiveSensorFragment.create(Sensor.TYPE_MAGNETIC_FIELD, "Magnetic field magnitude", "uT"));
        addTab(tabStrip, R.string.screen_accelerometer,
                () -> MotionSensorFragment.create(Sensor.TYPE_ACCELEROMETER, "Accelerometer", "m/s2"));
        addTab(tabStrip, R.string.screen_gravity,
                () -> MotionSensorFragment.create(Sensor.TYPE_GRAVITY, "Gravity", "m/s2"));
        addTab(tabStrip, R.string.screen_gyroscope,
                () -> MotionSensorFragment.create(Sensor.TYPE_GYROSCOPE, "Gyroscope", "rad/s"));
        addTab(tabStrip, R.string.screen_steps, () -> new StepCounterFragment());
        addTab(tabStrip, R.string.screen_compass, () -> new CompassFragment());
        addTab(tabStrip, R.string.screen_activity, () -> new ActivityRecognitionFragment());
    }

    private void addTab(LinearLayout tabStrip, int labelRes, FragmentFactory factory) {
        Button button = new Button(this);
        button.setAllCaps(false);
        button.setText(labelRes);
        button.setTextSize(13);
        button.setMinHeight(dp(44));
        button.setPadding(dp(14), 0, dp(14), 0);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(dp(4), 0, dp(4), 0);
        tabStrip.addView(button, params);

        int index = navigationButtons.size();
        navigationButtons.add(button);
        button.setOnClickListener(view -> showScreen(index, factory.create(), getString(labelRes)));
    }

    private void showScreen(int selectedIndex, Fragment fragment, String title) {
        titleView.setText(title);
        for (int i = 0; i < navigationButtons.size(); i++) {
            Button button = navigationButtons.get(i);
            boolean selected = i == selectedIndex;
            button.setTextColor(ContextCompat.getColor(this, selected ? R.color.white : R.color.ink_primary));
            button.setBackgroundColor(ContextCompat.getColor(this,
                    selected ? R.color.accent_sensor : R.color.surface_soft));
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_host, fragment)
                .commit();
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    private interface FragmentFactory {
        Fragment create();
    }
}
