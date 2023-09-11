package com.vibrationmeter.vibrometer.seismometer.measurevibration.checkfrequency.vibration.listener;

public interface AccelerometerListener {
    public void onAccelerationChanged(float x, float y, float z);

    public void onShake(float force);
}
