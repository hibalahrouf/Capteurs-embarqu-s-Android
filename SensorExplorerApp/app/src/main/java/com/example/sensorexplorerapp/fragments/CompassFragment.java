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

public class CompassFragment extends Fragment implements SensorEventListener {

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final SampleSimulator simulator = new SampleSimulator();
    private final float[] gravityValues = new float[3];
    private final float[] magneticValues = new float[3];
    private boolean hasGravity;
    private boolean hasMagnetic;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private TextView statusView;
    private TextView headingView;

    private final Runnable simulationLoop = new Runnable() {
        @Override
        public void run() {
            showHeading(simulator.nextHeading(), true);
            handler.postDelayed(this, 600);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ScrollView page = ViewTools.page(requireContext());
        LinearLayout content = (LinearLayout) page.getChildAt(0);
        content.addView(ViewTools.heading(requireContext(), "Compass"));

        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        statusView = ViewTools.body(requireContext(), "");
        headingView = ViewTools.panel(requireContext(), "Waiting for accelerometer and magnetometer data...");
        content.addView(statusView);
        content.addView(headingView);
        content.addView(ViewTools.panel(requireContext(),
                "Accelerometer\n" + SensorDetails.describe(accelerometer)
                        + "\n\nMagnetometer\n" + SensorDetails.describe(magnetometer)));
        return page;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (accelerometer != null && magnetometer != null) {
            statusView.setText("Using accelerometer and magnetometer for orientation.");
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
            sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_GAME);
        } else {
            statusView.setText("Compass sensors unavailable. Simulation values are active.");
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
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, gravityValues, 0, Math.min(event.values.length, 3));
            hasGravity = true;
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, magneticValues, 0, Math.min(event.values.length, 3));
            hasMagnetic = true;
        }

        if (hasGravity && hasMagnetic) {
            float[] rotation = new float[9];
            float[] orientation = new float[3];
            if (SensorManager.getRotationMatrix(rotation, null, gravityValues, magneticValues)) {
                SensorManager.getOrientation(rotation, orientation);
                float degrees = (float) Math.toDegrees(orientation[0]);
                if (degrees < 0f) {
                    degrees += 360f;
                }
                showHeading(degrees, false);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void showHeading(float degrees, boolean simulated) {
        headingView.setText((simulated ? "Simulated heading\n" : "Live heading\n")
                + String.format("%.1f deg\n%s", degrees, cardinalName(degrees)));
    }

    private String cardinalName(float degrees) {
        String[] names = {"North", "North East", "East", "South East", "South", "South West", "West", "North West"};
        int index = Math.round(degrees / 45f) % names.length;
        return names[index];
    }
}
