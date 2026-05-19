package com.example.sensorexplorerapp.utils;

import android.hardware.Sensor;

public class SampleSimulator {

    private float tick;

    public float nextScalar(int sensorType) {
        tick += 0.18f;
        switch (sensorType) {
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                return 22.5f + (float) Math.sin(tick) * 1.8f;
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                return 48f + (float) Math.sin(tick * 0.7f) * 9f;
            case Sensor.TYPE_PROXIMITY:
                return (Math.sin(tick) > 0.25f) ? 1.5f : 6f;
            case Sensor.TYPE_MAGNETIC_FIELD:
                return 42f + (float) Math.sin(tick * 0.8f) * 12f;
            case Sensor.TYPE_STEP_COUNTER:
                return Math.max(0f, tick * 1.7f);
            default:
                return (float) Math.sin(tick) * 5f;
        }
    }

    public float[] nextVector(int sensorType) {
        tick += 0.16f;
        float wave = (float) Math.sin(tick);
        float side = (float) Math.cos(tick * 0.8f);
        switch (sensorType) {
            case Sensor.TYPE_ACCELEROMETER:
                return new float[]{wave * 1.3f, side * 0.9f, 9.8f + wave * 0.7f};
            case Sensor.TYPE_GRAVITY:
                return new float[]{wave * 0.3f, side * 0.3f, 9.8f};
            case Sensor.TYPE_GYROSCOPE:
                return new float[]{wave * 0.6f, side * 0.35f, (float) Math.sin(tick * 1.4f) * 0.25f};
            case Sensor.TYPE_MAGNETIC_FIELD:
                return new float[]{28f + wave * 5f, side * 14f, 38f + wave * 3f};
            default:
                return new float[]{wave, side, (float) Math.sin(tick * 1.5f)};
        }
    }

    public float nextHeading() {
        tick += 3.5f;
        if (tick >= 360f) {
            tick -= 360f;
        }
        return tick;
    }
}
