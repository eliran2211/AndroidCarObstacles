package com.example.carobstacles208490540.utilities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.carobstacles208490540.Interfaces.StepCallback;

public class CarStepDetector {

    private Sensor sensor;

    private SensorManager sensorManager;
    private float deviceWidth;
    private float iconWidth;

    private long timestamp = 0;

    private StepCallback stepCallback;
    private float x;

    private SensorEventListener sensorEventListener;

    public CarStepDetector(Context context, float deviceWidth, float iconWidth, StepCallback stepCallback) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.deviceWidth = deviceWidth;
        this.iconWidth = iconWidth;
        this.stepCallback = stepCallback;
        initEventListener();
    }

    private void initEventListener() {
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float y = event.values[1];
                float x = event.values[0];
                calculateStep(x, y);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    private void calculateStep(float x, float y) {
        this.x = x;
        stepCallback.stepX();
    }

    public float getStepX() {
        return this.x;
    }

    public void start() {
        sensorManager.registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
        );
    }

    public void stop() {
        sensorManager.unregisterListener(
                sensorEventListener,
                sensor
        );
    }
}
