package com.example.sensorexplorerapp.fragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sensorexplorerapp.utils.SensorDetails;
import com.example.sensorexplorerapp.utils.ViewTools;

import java.util.List;

public class SensorsListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ScrollView page = ViewTools.page(requireContext());
        LinearLayout content = (LinearLayout) page.getChildAt(0);
        content.addView(ViewTools.heading(requireContext(), "Available sensors"));

        SensorManager sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        content.addView(ViewTools.body(requireContext(), sensors.size() + " sensor entries reported by Android."));

        for (Sensor sensor : sensors) {
            TextView card = ViewTools.panel(requireContext(), SensorDetails.describe(sensor));
            content.addView(card);
        }

        if (sensors.isEmpty()) {
            content.addView(ViewTools.panel(requireContext(), "No sensors were reported by this device."));
        }
        return page;
    }
}
