# SensorExplorerApp

Android Sensors lab implemented in Java.

Package name:

```text
com.example.sensorexplorerapp
```

## What The App Does

SensorExplorerApp demonstrates Android sensor access with `SensorManager` and `SensorEventListener`.

Implemented screens:

- Sensors list with technical details
- Temperature graph
- Humidity graph
- Proximity graph
- Magnetic field graph
- Accelerometer graph
- Gravity graph
- Gyroscope graph
- Step counter with `ACTIVITY_RECOGNITION` permission
- Compass using accelerometer + magnetometer
- Simple activity recognition using accelerometer

The app uses fragments for each screen and keeps helper code organized in:

```text
app/src/main/java/com/example/sensorexplorerapp/
  fragments/
  utils/
  views/
```

## Main Files

- `MainActivity.java`: hosts navigation and loads fragments.
- `fragments/SensorsListFragment.java`: lists all available sensors.
- `fragments/LiveSensorFragment.java`: reusable graph screen for temperature, humidity, proximity, and magnetic field.
- `fragments/MotionSensorFragment.java`: reusable graph screen for accelerometer, gravity, and gyroscope.
- `fragments/StepCounterFragment.java`: step counter and runtime permission handling.
- `fragments/CompassFragment.java`: compass heading from accelerometer and magnetometer.
- `fragments/ActivityRecognitionFragment.java`: simple movement classification.
- `utils/SensorDetails.java`: formats sensor technical information.
- `utils/SampleSimulator.java`: generates fallback values when a sensor is unavailable.
- `views/LineChartView.java`: custom live graph view.

## How To Run

1. Open the project in Android Studio.
2. Check the Android SDK path:

   ```text
   File > Project Structure > SDK Location
   ```

3. Let Gradle sync.
4. Run the app on an emulator or real Android device.

You can also build from the terminal:

```powershell
.\gradlew.bat assembleDebug
```

If Gradle says the SDK location is missing, fix `local.properties` through Android Studio's SDK settings.

## How To Test The Lab

### Sensors List

Open **Sensors**.

Expected result:

- All available sensors are listed.
- Each sensor shows name, vendor, version, string type, int type, resolution, power, maximum range, minimum delay, and reporting mode.

### Live Graph Sensors

Open these screens:

- **Temperature**
- **Humidity**
- **Proximity**
- **Magnetic Field**

Expected result:

- The live value is displayed.
- The custom graph updates.
- If the emulator/device does not provide the sensor, simulated values are shown.

### Motion Sensors

Open these screens:

- **Accelerometer**
- **Gravity**
- **Gyroscope**

Expected result:

- X, Y, and Z values are displayed.
- The graph shows vector magnitude.
- If the sensor is unavailable, simulation mode is used.

### Step Counter

Open **Steps**.

Expected result:

- On Android 10 or newer, the app requests `ACTIVITY_RECOGNITION`.
- If permission is granted and the device has a step counter, it shows:
  - steps since device boot
  - steps during this app session
- If unavailable or permission is denied, simulated step values are shown.

Note: the hardware step counter is best tested on a real phone.

### Compass

Open **Compass**.

Expected result:

- The heading is shown in degrees.
- A cardinal direction is shown, such as North, East, South, or West.
- If accelerometer or magnetometer is unavailable, simulated heading values are shown.

Best test: hold a real phone flat and rotate it.

### Activity Recognition

Open **Activity**.

Try these actions:

- keep the phone still
- move it slowly
- walk with it
- shake or jump lightly

Expected labels include:

- Sitting or still
- Standing or light movement
- Walking
- Jumping or strong movement

## Emulator Testing

In Android Emulator, open:

```text
Extended Controls > Virtual sensors
```

Use virtual sensors to test accelerometer, magnetometer, proximity, and other supported values.

Some emulator images do not support every sensor. This app includes simulated fallback values so the lab screens remain testable.

## Manual Notes

- If Android Studio shows SDK errors, set the SDK path manually in Project Structure.
- If the step counter does not work on the emulator, use a real device or rely on the app's simulation mode.
- If a graph shows simulated values, that means the corresponding hardware sensor is not available on the current device/emulator.
