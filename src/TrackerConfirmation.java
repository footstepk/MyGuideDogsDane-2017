package com.ospit.heng.myguidedogsdane;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


import java.util.List;
import java.util.Timer;

/**
 * Author by Kok Heng on 11/04/2017
 */
  * this class to check the route information
  * to confirmation the route activity
  /*

public class TrackerConfirmation extends Activity implements SensorEventListener {
    TextToSpeech t1;
    private float mLastX, mLastY, mLastZ;
    private boolean mInitialized;
    private SensorManager mSensorManager;
    private final float NOISE = (float) 8.0;

    private static final String SENSOR_SERVICE = Context.SENSOR_SERVICE;
    private SensorManager sensorMgr;
    private Sensor mAccelerometer;
    private boolean accelSupported;
    private long timeInMillis;
    private long threshold;
    private OnShakerTreshold listener;
    ArrayList<Float> valueStack;
    Context mcontext;
    DBhelper mydb;


    public TrackerConfirmation(Context context, long timeInMillis, long threshold) {
        try {
            this.timeInMillis = timeInMillis;
            this.threshold = threshold;
            this.listener = listener;
            if (timeInMillis<100){
                throw new Exception("timeInMillis < 100ms");
            }
            valueStack = new ArrayList<Float>((int)(timeInMillis/100));
            sensorMgr = (SensorManager) context.getSystemService(SENSOR_SERVICE);
            mAccelerometer = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mcontext = context;


        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void start() {

        try {
            accelSupported = sensorMgr.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
            if (!accelSupported) {
                stop();
                speak("Shake sensor is not supported");
                throw new Exception("Sensor is not supported");

            } else {

                speak("Shake left right to confirm record, shake up down to cancel record.");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void stop(){
        try {
            sensorMgr.unregisterListener(this, mAccelerometer);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            stop();
        } catch (Exception e){
            e.printStackTrace();
        }
        super.finalize();
    }

    long lastUpdate = 0;
    private float last_x;
    private float last_y;
    private float last_z;

    public void onSensorChanged(SensorEvent event) {
        try {
            if (event.sensor == mAccelerometer) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                if (!mInitialized) {
                    mLastX = x;
                    mLastY = y;
                    mLastZ = z;
                    mInitialized = true;
                } else {
                    float deltaX = Math.abs(mLastX - x);
                    float deltaY = Math.abs(mLastY - y);
                    float deltaZ = Math.abs(mLastZ - z);
                    if (deltaX < NOISE) deltaX = (float)0.0;
                    if (deltaY < NOISE) deltaY = (float)0.0;
                    if (deltaZ < NOISE) deltaZ = (float)0.0;
                    mLastX = x;
                    mLastY = y;
                    mLastZ = z;
                    if (deltaX > deltaY) {
                        stop();
                        MainActivity.alert.dismiss();
                        speak("Shake left right detected. Your record saved.");

                    } else if (deltaY > deltaX) {
                        stop();
                        //mydb = new DBhelper(this);
                        //mydb.deleteLocationdata(global.recordid);
                        MainActivity.alert.dismiss();
                        speak("Shake up down detected.Your record saved.");

                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    private long getNumberOfMeasures() {
        return timeInMillis/100;
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    public interface OnShakerTreshold {
        public void onTreshold();
    }

    void speak(String input){

        MainActivity.speak(input);
    }

}