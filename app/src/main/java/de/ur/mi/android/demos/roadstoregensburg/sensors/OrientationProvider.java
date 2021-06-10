package de.ur.mi.android.demos.roadstoregensburg.sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class OrientationProvider implements SensorEventListener {

    private final SensorManager sensorManager;
    private OrientationProviderListener listener;
    private final float[] accelerometerReading = new float[3];
    private final float[] magnetometerReading = new float[3];

    public OrientationProvider(SensorManager sensorManager, OrientationProviderListener listener) {
        this.sensorManager = sensorManager;
        this.listener = listener;
    }

    public void start() throws OrientationProviderException {
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor magneticFieldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (accelerometerSensor == null || magneticFieldSensor == null) {
            throw new OrientationProviderException("Could not retrieve all sensors");
        }
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, magneticFieldSensor, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        updateReadings(event);
        float currentAzimuth = getAzimuth(accelerometerReading, magnetometerReading);
        listener.onAzimuthChange(currentAzimuth);
    }

    private void updateReadings(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.length);
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.length);
        }
    }

    private float getAzimuth(float[] lastAccelerometerReading, float[] lastMagnetometerReading) {
        final float[] rotationMatrix = new float[9];
        final float[] orientationAngles = new float[3];
        SensorManager.getRotationMatrix(rotationMatrix, null, lastAccelerometerReading, lastMagnetometerReading);
        SensorManager.getOrientation(rotationMatrix, orientationAngles);
        float azimuth = orientationAngles[0];
        return (float) Math.toDegrees(azimuth);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public interface OrientationProviderListener {
        void onAzimuthChange(float azimuth);
    }

    public static class OrientationProviderException extends Exception {
        public OrientationProviderException(String could_not_retrieve_all_sensors) {
        }
    }
}
