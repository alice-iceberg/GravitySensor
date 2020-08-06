package com.aliceberg.gravitysensor;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView textView_x, textView_y, textView_z;
    private SensorManager sensorManager;
    private Sensor mGravity;
    private boolean isGravitySensorPresent;

    Handler cameraHandler = new Handler();






    //y is showing vertical position! value[1]

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        textView_x = findViewById(R.id.xTextView);
        textView_y = findViewById(R.id.yTextView);
        textView_z = findViewById(R.id.zTextView);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

            manager.registerAvailabilityCallback(new CameraManager.AvailabilityCallback() {
                @Override
                public void onCameraAvailable(String cameraId) {
                    super.onCameraAvailable(cameraId);
                    Log.e("TAG", "onCameraAvailable: Camera off");
                    //Do your work
                }

                @Override
                public void onCameraUnavailable(String cameraId) {
                    super.onCameraUnavailable(cameraId);
                    Log.e("TAG", "onCameraUnavailable: Camera on");
                    //Do your work
                }
            }, cameraHandler);
        }

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) != null) {
            mGravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
            isGravitySensorPresent = true;
        } else {
            textView_x.setText("Sensor is not present");
            isGravitySensorPresent = false;
        }


    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        textView_x.setText(sensorEvent.values[0] + "m/s2");
        textView_y.setText(sensorEvent.values[1] + "m/s2");
        textView_z.setText(sensorEvent.values[2] + "m/s2");

        if (sensorEvent.values[2] < -9.7) {
            getWindow().getDecorView().setBackgroundColor(Color.GREEN);
        } else {
            getWindow().getDecorView().setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) != null)
            sensorManager.registerListener(this, mGravity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) != null)
            sensorManager.unregisterListener(this, mGravity);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}