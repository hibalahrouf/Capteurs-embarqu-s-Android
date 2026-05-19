package com.example.sensorexplorerapp.fragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sensorexplorerapp.utils.SampleSimulator;
import com.example.sensorexplorerapp.utils.SensorDetails;
import com.example.sensorexplorerapp.utils.ViewTools;
import com.example.sensorexplorerapp.views.LineChartView;

public class LiveSensorFragment extends Fragment implements SensorEventListener {

    private static final String ARG_TYPE = "type";
    private static final String ARG_TITLE = "title";
    private static final String ARG_UNIT = "unit";

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final SampleSimulator simulator = new SampleSimulator();
    private SensorManager sensorManager;
    private Sensor sensor;
    private LineChartView chartView;
    private TextView readingView;
    private TextView statusView;
    private int sensorType;
    private String screenTitle;
    private String unit;

    private final Runnable simulationLoop = new Runnable() {
        @Override
        public void run() {
            float value = simulator.nextScalar(sensorType);
            showReading(value, true);
            handler.postDelayed(this, 450);
        }
    };

    public static LiveSensorFragment create(int sensorType, String title, String unit) {
        LiveSensorFragment fragment = new LiveSensorFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, sensorType);
        args.putString(ARG_TITLE, title);
        args.putString(ARG_UNIT, unit);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Bundle args = requireArguments();
        sensorType = args.getInt(ARG_TYPE);
        screenTitle = args.getString(ARG_TITLE, "Sensor");
        unit = args.getString(ARG_UNIT, "");

        ScrollView page = ViewTools.page(requireContext());
        LinearLayout content = (LinearLayout) page.getChildAt(0);
        content.addView(ViewTools.heading(requireContext(), screenTitle));

        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(sensorType);

        statusView = ViewTools.body(requireContext(), "");
        readingView = ViewTools.panel(requireContext(), "Waiting for sensor data...");
        chartView = new LineChartView(requireContext());
        chartView.setChartLabel(screenTitle + " (" + unit + ")");
        chartView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewTools.dp(requireContext(), 240)
        ));

        content.addView(statusView);
        content.addView(readingView);
        content.addView(chartView);
        content.addView(ViewTools.panel(requireContext(), SensorDetails.describe(sensor)));
        return page;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sensor != null) {
            statusView.setText("Using real device sensor: " + sensor.getName());
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            statusView.setText("Hardware sensor unavailable. Simulation values are active for emulator testing.");
            handler.post(simulationLoop);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float value;
        if (event.values.length >= 3 && sensorType == Sensor.TYPE_MAGNETIC_FIELD) {
            value = magnitude(event.values);
        } else {
            value = event.values[0];
        }
        showReading(value, false);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void showReading(float value, boolean simulated) {
        chartView.pushSample(value);
        readingView.setText((simulated ? "Simulated value: " : "Live value: ")
                + String.format("%.2f %s", value, unit));
    }

    private float magnitude(float[] values) {
        return (float) Math.sqrt(values[0] * values[0] + values[1] * values[1] + values[2] * values[2]);
    }
}
