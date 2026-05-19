package com.example.sensorexplorerapp.utils;

import android.hardware.Sensor;

public final class SensorDetails {

    private SensorDetails() {
    }

    public static String describe(Sensor sensor) {
        if (sensor == null) {
            return "No hardware sensor is available for this screen.";
        }

        return "Name: " + sensor.getName() + "\n"
                + "Vendor: " + sensor.getVendor() + "\n"
                + "Version: " + sensor.getVersion() + "\n"
                + "String type: " + sensor.getStringType() + "\n"
                + "Int type: " + sensor.getType() + "\n"
                + "Id: " + sensor.getId() + "\n"
                + "Resolution: " + sensor.getResolution() + "\n"
                + "Power: " + sensor.getPower() + " mA\n"
                + "Maximum range: " + sensor.getMaximumRange() + "\n"
                + "Minimum delay: " + sensor.getMinDelay() + " us\n"
                + "Reporting mode: " + reportingModeName(sensor.getReportingMode());
    }

    private static String reportingModeName(int mode) {
        switch (mode) {
            case Sensor.REPORTING_MODE_CONTINUOUS:
                return "continuous";
            case Sensor.REPORTING_MODE_ON_CHANGE:
                return "on change";
            case Sensor.REPORTING_MODE_ONE_SHOT:
                return "one shot";
            case Sensor.REPORTING_MODE_SPECIAL_TRIGGER:
                return "special trigger";
            default:
                return "unknown (" + mode + ")";
        }
    }
}
