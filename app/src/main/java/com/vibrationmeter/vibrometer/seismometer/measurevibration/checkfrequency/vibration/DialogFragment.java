package com.vibrationmeter.vibrometer.seismometer.measurevibration.checkfrequency.vibration;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;


public class DialogFragment extends androidx.fragment.app.DialogFragment implements SensorEventListener {
    private float[] gravity = new float[3];
    public Vibrator vibrator;
    private SensorManager sensorManager;
    private float vibrateThreshold = 0;
    private Sensor accelerometer;
    LottieAnimationView lottieAnimation;
    private float[] linear_acceleration = new float[3];
    TextView textone, texttwo, textthree, textfour, textfive, textsix, textseven,
            texteight, textnine, textten, texteleven, texttwevele;
    private int fragment_dialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(fragment_dialog, null);
        initliazation(view);


        //        return inflater.inflate(R.layout.fragment_dialog, container, false);

        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            // success! we have an accelerometer

            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
           // sensorManager.registerListener((SensorEventListener) this.getActivity(), accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
            vibrateThreshold = accelerometer.getMaximumRange() / 2;
        } else {
            // fai! we dont have an accelerometer!
        }

        ;
        //initialize vibration
        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        return view;
//        return inflater.inflate(R.layout.fragment_dialog, container, false);

    }

    public void initliazation(View view) {

        textone = view.findViewById(R.id.first_title);
        texttwo = view.findViewById(R.id.second_title);
        textthree = view.findViewById(R.id.third_title);
        textfour = view.findViewById(R.id.four_title);
        textfive = view.findViewById(R.id.five_title);
        textsix = view.findViewById(R.id.six_title);
        textseven = view.findViewById(R.id.seven_title);
        texteight = view.findViewById(R.id.eight_title);
        textnine = view.findViewById(R.id.nine_title);
        textten = view.findViewById(R.id.ten_title);
        texteleven = view.findViewById(R.id.eleven_title);
        texttwevele = view.findViewById(R.id.twelve_title);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final float alpha = 0.8f;
//gravity is calculated here
        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];
//acceleration retrieved from the event and the gravity is removed
        linear_acceleration[0] = event.values[0] - gravity[0];
        linear_acceleration[1] = event.values[1] - gravity[1];
        linear_acceleration[2] = event.values[2] - gravity[2];

        double accelationSquareRoot = Math.sqrt(linear_acceleration[0] * linear_acceleration[0] +
                linear_acceleration[1] * linear_acceleration[1] +
                linear_acceleration[2] * linear_acceleration[2]) / SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH;

        if (accelationSquareRoot > 0.01) {
            textone.setTextColor(Color.RED);
            texttwo.setTextColor(Color.WHITE);
            textthree.setTextColor(Color.WHITE);
            textfour.setTextColor(Color.WHITE);
            textfive.setTextColor(Color.WHITE);
            textsix.setTextColor(Color.WHITE);
            textseven.setTextColor(Color.WHITE);
            texteight.setTextColor(Color.WHITE);
            textnine.setTextColor(Color.WHITE);
            texteleven.setTextColor(Color.WHITE);
        }
        if (accelationSquareRoot > 0.02) {
            textone.setTextColor(Color.WHITE);
            texttwo.setTextColor(Color.RED);
            textthree.setTextColor(Color.WHITE);
            textfour.setTextColor(Color.WHITE);
            textfive.setTextColor(Color.WHITE);
            textsix.setTextColor(Color.WHITE);
            textseven.setTextColor(Color.WHITE);
            texteight.setTextColor(Color.WHITE);
            textnine.setTextColor(Color.WHITE);
            texteleven.setTextColor(Color.WHITE);
        }
        if (accelationSquareRoot > 0.04) {
            textone.setTextColor(Color.WHITE);
            texttwo.setTextColor(Color.WHITE);
            textthree.setTextColor(Color.RED);
            textfour.setTextColor(Color.WHITE);
            textfive.setTextColor(Color.WHITE);
            textsix.setTextColor(Color.WHITE);
            textseven.setTextColor(Color.WHITE);
            texteight.setTextColor(Color.WHITE);
            textnine.setTextColor(Color.WHITE);
            texteleven.setTextColor(Color.WHITE);
        }
        if (accelationSquareRoot > 0.07) {
            textone.setTextColor(Color.WHITE);
            texttwo.setTextColor(Color.WHITE);
            textthree.setTextColor(Color.WHITE);
            textfour.setTextColor(Color.RED);
            textfive.setTextColor(Color.WHITE);
            textsix.setTextColor(Color.WHITE);
            textseven.setTextColor(Color.WHITE);
            texteight.setTextColor(Color.WHITE);
            textnine.setTextColor(Color.WHITE);
            texteleven.setTextColor(Color.WHITE);
        }

        if (accelationSquareRoot > 0.15) {
            textone.setTextColor(Color.WHITE);
            texttwo.setTextColor(Color.WHITE);
            textthree.setTextColor(Color.WHITE);
            textfour.setTextColor(Color.WHITE);
            textfive.setTextColor(Color.RED);
            textsix.setTextColor(Color.WHITE);
            textseven.setTextColor(Color.WHITE);
            texteight.setTextColor(Color.WHITE);
            textnine.setTextColor(Color.WHITE);
            texteleven.setTextColor(Color.WHITE);
        }
        if (accelationSquareRoot > 1.0) {
            textone.setTextColor(Color.WHITE);
            texttwo.setTextColor(Color.WHITE);
            textthree.setTextColor(Color.WHITE);
            textfour.setTextColor(Color.WHITE);
            textfive.setTextColor(Color.WHITE);
            textsix.setTextColor(Color.RED);
            textseven.setTextColor(Color.WHITE);
            texteight.setTextColor(Color.WHITE);
            textnine.setTextColor(Color.WHITE);
            texteleven.setTextColor(Color.WHITE);


        }

        if (accelationSquareRoot > 3.0) {
            textone.setTextColor(Color.WHITE);
            texttwo.setTextColor(Color.WHITE);
            textthree.setTextColor(Color.WHITE);
            textfour.setTextColor(Color.WHITE);
            textsix.setTextColor(Color.WHITE);
            textseven.setTextColor(Color.RED);

            texteight.setTextColor(Color.WHITE);
            textnine.setTextColor(Color.WHITE);
            texteleven.setTextColor(Color.WHITE);
        }
        if (accelationSquareRoot > 6.0) {
            textone.setTextColor(Color.WHITE);
            texttwo.setTextColor(Color.WHITE);
            textthree.setTextColor(Color.WHITE);
            textfour.setTextColor(Color.WHITE);
            textsix.setTextColor(Color.WHITE);
            textseven.setTextColor(Color.WHITE);
            texteight.setTextColor(Color.WHITE);
            texteight.setTextColor(Color.RED);

            textnine.setTextColor(Color.WHITE);
            texteleven.setTextColor(Color.WHITE);
        }
        if (accelationSquareRoot > 10.0) {
            textone.setTextColor(Color.WHITE);
            texttwo.setTextColor(Color.WHITE);
            textthree.setTextColor(Color.WHITE);
            textfour.setTextColor(Color.WHITE);
            textsix.setTextColor(Color.WHITE);
            textseven.setTextColor(Color.WHITE);
            texteight.setTextColor(Color.WHITE);

            textnine.setTextColor(Color.RED);
            texteleven.setTextColor(Color.WHITE);
        }
        if (accelationSquareRoot > 14.0) {
            textone.setTextColor(Color.WHITE);
            texttwo.setTextColor(Color.WHITE);
            textthree.setTextColor(Color.WHITE);
            textfour.setTextColor(Color.WHITE);
            textsix.setTextColor(Color.WHITE);
            textseven.setTextColor(Color.WHITE);
            texteight.setTextColor(Color.WHITE);
            textnine.setTextColor(Color.WHITE);
            texteleven.setTextColor(Color.RED);
        }

        // if the change is below 2, it is just plain noise
       /* if (deltaX < 2)
            deltaX = 0;
        if (deltaY < 2)
            deltaY = 0;
        if ((deltaZ > vibrateThreshold) || (deltaY > vibrateThreshold) || (deltaZ > vibrateThreshold)) {
            vibrator.vibrate(50);
        }*/
    }
}