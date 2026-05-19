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

import java.util.ArrayDeque;
import java.util.Queue;

public class ActivityRecognitionFragment extends Fragment implements SensorEventListener {

    private static final float FILTER = 0.82f;
    private static final int WINDOW_SIZE = 28;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final SampleSimulator simulator = new SampleSimulator();
    private final float[] gravity = new float[3];
    private final Queue<Float> motionWindow = new ArrayDeque<>();
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private TextView statusView;
    private TextView resultView;
    private LineChartView chartView;

    private final Runnable simulationLoop = new Runnable() {
        @Override
        public void run() {
            processAcceleration(simulator.nextVector(Sensor.TYPE_ACCELEROMETER), true);
            handler.postDelayed(this, 260);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ScrollView page = ViewTools.page(requireContext());
        LinearLayout content = (LinearLayout) page.getChildAt(0);
        content.addView(ViewTools.heading(requireContext(), "Activity recognition"));

        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        statusView = ViewTools.body(requireContext(), "");
        resultView = ViewTools.panel(requireContext(), "Waiting for movement samples...");
        chartView = new LineChartView(requireContext());
        chartView.setChartLabel("Motion intensity");
        chartView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewTools.dp(requireContext(), 230)
        ));

        content.addView(statusView);
        content.addView(resultView);
        content.addView(chartView);
        content.addView(ViewTools.panel(requireContext(), SensorDetails.describe(accelerometer)));
        return page;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (accelerometer != null) {
            statusView.setText("Using accelerometer with a simple gravity filter.");
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        } else {
            statusView.setText("Accelerometer unavailable. Simulation values are active.");
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
        processAcceleration(event.values, false);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void processAcceleration(float[] values, boolean simulated) {
        for (int i = 0; i < 3; i++) {
            gravity[i] = FILTER * gravity[i] + (1f - FILTER) * values[i];
        }

        float x = values[0] - gravity[0];
        float y = values[1] - gravity[1];
        float z = values[2] - gravity[2];
        float intensity = (float) Math.sqrt(x * x + y * y + z * z);

        motionWindow.add(intensity);
        while (motionWindow.size() > WINDOW_SIZE) {
            motionWindow.poll();
        }

        float average = 0f;
        for (float sample : motionWindow) {
            average += sample;
        }
        average = motionWindow.isEmpty() ? 0f : average / motionWindow.size();

        chartView.pushSample(average);
        resultView.setText((simulated ? "Simulated activity\n" : "Detected activity\n")
                + classify(average) + "\n"
                + String.format("motion score: %.2f", average));
    }

    private String classify(float score) {
        if (score < 0.35f) {
            return "Sitting or still";
        }
        if (score < 1.3f) {
            return "Standing or light movement";
        }
        if (score < 4.2f) {
            return "Walking";
        }
        return "Jumping or strong movement";
    }
}
