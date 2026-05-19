package com.example.sensorexplorerapp.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.sensorexplorerapp.utils.SampleSimulator;
import com.example.sensorexplorerapp.utils.SensorDetails;
import com.example.sensorexplorerapp.utils.ViewTools;

public class StepCounterFragment extends Fragment implements SensorEventListener {

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final SampleSimulator simulator = new SampleSimulator();
    private SensorManager sensorManager;
    private Sensor stepSensor;
    private TextView statusView;
    private TextView countView;
    private float sessionStart = -1f;

    private final ActivityResultLauncher<String> permissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> beginListening());

    private final Runnable simulationLoop = new Runnable() {
        @Override
        public void run() {
            float total = simulator.nextScalar(Sensor.TYPE_STEP_COUNTER);
            showSteps(total, true);
            handler.postDelayed(this, 900);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ScrollView page = ViewTools.page(requireContext());
        LinearLayout content = (LinearLayout) page.getChildAt(0);
        content.addView(ViewTools.heading(requireContext(), "Step counter"));

        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        statusView = ViewTools.body(requireContext(), "");
        countView = ViewTools.panel(requireContext(), "Waiting for step counter...");
        content.addView(statusView);
        content.addView(countView);
        content.addView(ViewTools.panel(requireContext(), SensorDetails.describe(stepSensor)));
        return page;
    }

    @Override
    public void onResume() {
        super.onResume();
        requestPermissionIfNeeded();
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
        showSteps(event.values[0], false);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void requestPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
                && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED) {
            statusView.setText("Activity recognition permission is required for the hardware step counter.");
            permissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION);
            return;
        }
        beginListening();
    }

    private void beginListening() {
        if (stepSensor != null && hasPermission()) {
            statusView.setText("Using real step counter: " + stepSensor.getName());
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            statusView.setText("Step counter unavailable or permission denied. Simulation values are active.");
            handler.post(simulationLoop);
        }
    }

    private boolean hasPermission() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
                || ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACTIVITY_RECOGNITION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void showSteps(float bootTotal, boolean simulated) {
        if (sessionStart < 0f) {
            sessionStart = bootTotal;
        }
        int sessionSteps = Math.max(0, Math.round(bootTotal - sessionStart));
        countView.setText((simulated ? "Simulated step stream\n" : "Hardware step stream\n")
                + "Steps since device boot: " + Math.round(bootTotal) + "\n"
                + "Steps this session: " + sessionSteps);
    }
}
