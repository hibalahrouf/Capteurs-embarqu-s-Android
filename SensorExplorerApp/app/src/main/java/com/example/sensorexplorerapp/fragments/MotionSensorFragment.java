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

public class MotionSensorFragment extends Fragment implements SensorEventListener {

    private static final String ARG_TYPE = "type";
    private static final String ARG_TITLE = "title";
    private static final String ARG_UNIT = "unit";

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final SampleSimulator simulator = new SampleSimulator();
    private SensorManager sensorManager;
    private Sensor sensor;
    private LineChartView chartView;
    private TextView vectorView;
    private TextView statusView;
    private int sensorType;
    private String title;
    private String unit;

    private final Runnable simulationLoop = new Runnable() {
        @Override
        public void run() {
            updateVector(simulator.nextVector(sensorType), true);
            handler.postDelayed(this, 420);
        }
    };

    public static MotionSensorFragment create(int sensorType, String title, String unit) {
        MotionSensorFragment fragment = new MotionSensorFragment();
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
        title = args.getString(ARG_TITLE, "Motion sensor");
        unit = args.getString(ARG_UNIT, "");

        ScrollView page = ViewTools.page(requireContext());
        LinearLayout content = (LinearLayout) page.getChildAt(0);
        content.addView(ViewTools.heading(requireContext(), title));

        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(sensorType);

        statusView = ViewTools.body(requireContext(), "");
        vectorView = ViewTools.panel(requireContext(), "Waiting for x/y/z values...");
        chartView = new LineChartView(requireContext());
        chartView.setChartLabel(title + " vector magnitude");
        chartView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewTools.dp(requireContext(), 240)
        ));

        content.addView(statusView);
        content.addView(vectorView);
        content.addView(chartView);
        content.addView(ViewTools.panel(requireContext(), SensorDetails.describe(sensor)));
        return page;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sensor != null) {
            statusView.setText("Using real device sensor: " + sensor.getName());
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
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
        updateVector(event.values, false);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void updateVector(float[] values, boolean simulated) {
        float x = values.length > 0 ? values[0] : 0f;
        float y = values.length > 1 ? values[1] : 0f;
        float z = values.length > 2 ? values[2] : 0f;
        float total = (float) Math.sqrt(x * x + y * y + z * z);
        chartView.pushSample(total);
        vectorView.setText((simulated ? "Simulated vector\n" : "Live vector\n")
                + String.format("x: %.2f %s\ny: %.2f %s\nz: %.2f %s\nmagnitude: %.2f",
                x, unit, y, unit, z, unit, total));
    }
}
