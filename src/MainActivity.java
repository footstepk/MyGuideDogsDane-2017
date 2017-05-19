package com.ospit.heng.myguidedogsdane;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.speech.tts.TextToSpeech;
import android.speech.RecognizerIntent;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.Constants;
import com.mapbox.services.android.telemetry.permissions.PermissionsListener;
import com.mapbox.services.android.telemetry.permissions.PermissionsManager;
import com.mapbox.services.api.ServicesException;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.api.directions.v5.DirectionsCriteria;
import com.mapbox.services.api.directions.v5.MapboxDirections;
import com.mapbox.services.api.directions.v5.models.DirectionsResponse;
import com.mapbox.services.api.directions.v5.models.DirectionsRoute;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;


import org.json.JSONObject;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements SensorEventListener, GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener,PermissionsListener,ConnectionCallbacks,OnConnectionFailedListener  {

    private TextView mTextMessage;
    private LocationManager locationManager;
    private LocationListener listener;
    Intent intent;
    static TextToSpeech t1;
    private Context mContext;
    DBhelper mydb;
    static AlertDialog alert;
    private static final String DEBUG_TAG = "Gestures";
    private GestureDetectorCompat mDetector;
    private Handler mHandler = new Handler();
    private MapView mapview;
    private FrameLayout fr1;
    private FrameLayout fr2;
    private MapView mapView;

    private MapboxMap map;
    private static final String TAG = "DirectionsActivity";
    private DirectionsRoute currentRoute;
    private RecyclerView recyclerView;
    private PermissionsManager permissionsManager;
    private GoogleApiClient mGoogleApiClient;

    private float azimut = 0f;
    private SensorManager mSensorManager;
    Sensor accelerometer;
    Sensor magnetometer;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {


                // define function for bottom navigation.

                case R.id.navigation_home:
                  //  mTextMessage.setText(R.string.title_home);
                    speak("My location");
                    mTextMessage.setText("My Location");
                    configure_button();
                    return true;
                case R.id.navigation_dashboard:

                    try{
                    global.data = new ArrayList<ArrayList<String>>();
                        mydb = new DBhelper(mContext);
                    global.data = mydb.getLocationFile();

                    int i = 0;
                    global.datasize = global.data.size();

                        if (global.datasize == 0){
                            speak("You do not have saved record.");
                        }else {

                            ArrayList<String> dataitem = global.data.get(global.selectedIndex);
                            String id = dataitem.get(0);
                            String recordname = dataitem.get(1);
                            String recordid = dataitem.get(2);

                            global.sRecordName = recordname;
                            global.sRecordID = recordid;


                            global.action = "selectRecord";
                            global.selectedIndex = 0;
                            speak("You have " + String.valueOf(global.datasize) + " record. Scroll screen to navigate thru your data.Tap screen to access data and long press exit data selection.");
                        }
                        } catch (Exception e) {
                        String result = e.toString();
                        Log.v("log_tag", "Error in DB connection : " + e.toString());
                    }

                    return true;


                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    speak("Get places at your location.");
                    global.action="places0";
                    configure_button();
                    return true;
            }
            return false;
        }

    };

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        // Be sure to call the superclass implementation
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent event) {
        Log.d(DEBUG_TAG,"onDown: " + event.toString());
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {

        // detect swipe screen motion.

        if (global.action == "selectRecord") {

            // if global define as select record, function will add navigate thru record array.

            ArrayList<String> dataitem = global.data.get(global.selectedIndex);
            String id = dataitem.get(0);
            String recordname = dataitem.get(1);
            String recordid = dataitem.get(2);

            global.sRecordName = recordname;
            global.sRecordID = recordid;

            speak("Record name at " + String.valueOf(global.selectedIndex + 1) + " :" + recordname);

            int nextrecord = global.selectedIndex + 1;

            if (nextrecord == global.datasize) {
                global.selectedIndex = 0;
            } else {

                global.selectedIndex = nextrecord;
            }

        }

        if (global.action == "places") {

            // if global action is define as places, it will navigate thru data from google map. places nearby

            ArrayList<String> dataitem = global.data.get(global.selectedIndex);
            String id = dataitem.get(0);
            String name = dataitem.get(1);

            global.sRecordName = name;
            global.sRecordID = id;

            speak("Record name at " + String.valueOf(global.selectedIndex + 1) + " :" + name);

            int nextrecord = global.selectedIndex + 1;

            if (nextrecord == global.datasize) {
                global.selectedIndex = 0;
            } else {

                global.selectedIndex = nextrecord;
            }

        }

        Log.d(DEBUG_TAG, "onFling: " + event1.toString()+event2.toString());
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        Log.d(DEBUG_TAG, "onLongPress: " + event.toString());

        // detect long press activity

        if (global.action == "confirmrecord") {

            // if action is confirm record during select record action. It will cancel out.

            mydb = new DBhelper(mContext);
            mydb.deleteLocationdata(global.recordid);
            speak("Your record deleted.");

            global.action = "";
        }

        if (global.action == "confirmrecord2") {

            // if action is confirm record at step 2. it will fire up the speak record name again.

            speak("Speak record name.");

            Runnable mUpdateNameTask = new Runnable() {
                public void run() {
                    promptSpeechInput();
                }
            };

            mHandler.postDelayed(mUpdateNameTask, 2000);

        }

        if (global.action == "selectRecord"){

            // if action is select record , it will exit the select record function.

            speak("Exit data selection.");
            global.selectedIndex = 0;
            global.action = "";
            global.data.clear();
        }

        if (global.action == "navigate"){

            // if action is navigate, it will exit the navigate function.

            global.selectedIndex = 0;
            speak("Navigation stop.");
            global.action = "";
            global.data.clear();
        }

        if (global.action == "nearby"){

            // if function is nearby, it will exit the nearby function.

            global.dataNearby.clear();
            speak("Nearby record clear.");
        }

        if (global.action == "return"){

            // if function is return, it will exit the function.

            global.action="navigate";
            global.startNavigate = true;
            configure_button();
        }
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {


        Log.d(DEBUG_TAG, "onScroll: " + e1.toString()+e2.toString());

        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        Log.d(DEBUG_TAG, "onShowPress: " + event.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        Log.d(DEBUG_TAG, "onSingleTapUp: " + event.toString());

        // detect single tap on screen


        if (global.action == "return"){

            // if function is return. it will start navigate back to original way point.
            global.destination = global.origine;
            global.action="navigate";
            global.startNavigate = true;
            configure_button();
        }

        if (global.action == "confirmrecord") {

            // if function is confirmrecord, it will start to prompt for speaking record name.

            speak("speak record name.");

            Runnable mUpdateNameTask = new Runnable() {
                public void run() {
                    promptSpeechInput();
                }
            };

            mHandler.postDelayed(mUpdateNameTask, 2000);


        }

         if (global.action == "confirmrecord2") {

             // if funcion is confirm record at stage 2. it will update the record.

            try{
            mydb = new DBhelper(mContext);
            mydb.inserLocationName(global.recordid,global.recordname);
            global.action = "";

            speak("Your record saved.");

            } catch (Exception e) {
                String result = e.toString();
                Log.v("log_tag", "Error in DB connection : " + e.toString());
            }
        }

         if (global.action == "selectRecord"){

             // if function at select record. it will load the record information from DB to array and start data navigation.

            speak("You are selecting record "+ global.sRecordName +".");

            mydb = new DBhelper(mContext);
            global.dataRoute = mydb.getLocationData(global.sRecordID);

            int i =0;
           final int n = global.dataRoute.size();


             Position origin;
             Position destination;



                     for (int j = 0;  j < n ; j++) {

                         ArrayList<String> dataitem = global.dataRoute.get(j);
                         String id = dataitem.get(0);
                         String recordid = dataitem.get(1);
                         String altitude = dataitem.get(2);
                         String longtitude = dataitem.get(3);
                         String timestamp = dataitem.get(4);

                            if (j == 0){
                               global.origine = Position.fromCoordinates(Double.parseDouble(longtitude),Double.parseDouble(altitude));
                            }

                            if (j == n-1){
                                global.destination = Position.fromCoordinates(Double.parseDouble(longtitude),Double.parseDouble(altitude));
                            }

                             Log.d(DEBUG_TAG, "Waypoint: " + altitude + " : " + longtitude);
                     }

            speak("Start navigation. Long press screen at any time to cancel the navigation.");
            global.action="navigate";
            global.startNavigate = true;
            configure_button();

        }

         if (global.action == "places"){

            speak("You are selecting record "+ global.sRecordName +".");

            Position origin;
            Position destination;

     ArrayList<String> dataitem = global.data.get(global.selectedIndex);
                String id = dataitem.get(0);
                String name = dataitem.get(1);
                String longtitude = dataitem.get(2);
                String latitude = dataitem.get(3);

                    global.destination = Position.fromCoordinates(Double.parseDouble(longtitude),Double.parseDouble(latitude));


            speak("Start navigation. Long press screen at any time to cancel the navigation.");
            global.action="navigate";
            global.startNavigate = true;
            configure_button();


        }

         if(global.action == "nearby"){

             // if function at nearby. it will load google map nearby places information to array for data navigation.

            mydb = new DBhelper(mContext);
            final int n = global.dataNearby.size();


            Position origin;
            Position destination;


            global.data = new ArrayList<ArrayList<String>>();

            for (int j = 0;  j < n ; j++) {

                String recordid = global.dataNearby.get(j);

                ArrayList<String> dataitem = mydb.getLocationFileByID(recordid);

                global.data.add(dataitem);

            }

            global.action = "selectRecord";
            speak("You have " + String.valueOf(n) + " record. Scroll screen to move around data. Tap screen to select data. Long press to cancel.");
        }
        return true;
    }



    @Override
    public boolean onDoubleTap(MotionEvent event) {

        if (global.action == "navigate"){
            speak("Do you want to return to your origine place? tap for yes. long press for no.");

            global.action = "return";

        }



        Log.d(DEBUG_TAG, "onDoubleTap: " + event.toString());
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        Log.d(DEBUG_TAG, "onDoubleTapEvent: " + event.toString());
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        Log.d(DEBUG_TAG, "onSingleTapConfirmed: " + event.toString());
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // on create function

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.titleheader);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // define function for record button.

        Button locationbtn = (Button) findViewById(R.id.button);

        locationbtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (isMyServiceRunning(GPStracker.class) == true){
                    stopService(intent);
                    speak("End tracking location...");
                    mTextMessage.setText("End tracking location...");

                    speak("Do you want to keep the tracking record? Tap screen for yes. Long press screen for No");


                    global.action = "confirmrecord";

                } else {
                    startService(intent);
                    speak("Start tracking location...");
                    mTextMessage.setText("Start tracking location...");
                }
            }
        }
            );

        // define location manager.

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                GeoLocationDecode(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        intent = new Intent(this, GPStracker.class);

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });

        mContext = getApplicationContext();

        // Instantiate the gesture detector with the
        // application context and an implementation of
        // GestureDetector.OnGestureListener
        mDetector = new GestureDetectorCompat(this,this);
        // Set the gesture detector as the double tap
        // listener.
        mDetector.setOnDoubleTapListener(this);


        // Check for location permission
        permissionsManager = new PermissionsManager(this);
        if (!PermissionsManager.areLocationPermissionsGranted(this)) {
           // recyclerView.setEnabled(false);
            permissionsManager.requestLocationPermissions(this);
        }

        // define sensor manager for compass function.
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        global.rotation = new ArrayList<String>();

        //define timer function for clearing compass data.

        TimerTask rotationEvent = new TimerTask(){
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (global.action == "navigate") {

                            int n = global.rotation.size();
                            if (n > 0){

                            speak(global.rotation.get(n - 1));

                            global.rotation.clear();
                            }
                        } else {
                            global.rotation.clear();}
                        }
                });

            }
        };

        Timer timer = new Timer();
        timer.schedule(rotationEvent,0,4000);

    }


    @Override
    public void onResume(){
        super.onResume();

        // on resume function.
        mSensorManager.registerListener(this, accelerometer, 100*1000*1000);
        mSensorManager.registerListener(this, magnetometer, 100*1000*1000);
    }

    @Override
    public void onPause(){

        // on pause function
        super.onPause();
        mSensorManager.unregisterListener(this);

    }

    float[] mGravity;
    float[] mGeomagnetic;

    @Override
    public void onSensorChanged(SensorEvent event){

        // on sensor change function for compass activity.

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;

        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            float outR[] = new float[9];

            if (SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic)) {

                // orientation contains azimut, pitch and roll
                float orientation[] = new float[3];
                SensorManager.remapCoordinateSystem(R, SensorManager.AXIS_X, SensorManager.AXIS_Y, outR);

                SensorManager.getOrientation(outR, orientation);
                azimut = orientation[0];

                float degree = (float)(Math.toDegrees(azimut)+360)%360;

                global.rotation.add(DegreesToCardinal(degree));
            }
        }
        }

    @Override
    public void onAccuracyChanged(Sensor sensor,int accuracy){

    }


    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, "This app needs location permissions in order to show its functionality.",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
           // recyclerView.setEnabled(true);
        } else {
            Toast.makeText(this, "You didn't grant location permissions.",
                    Toast.LENGTH_LONG).show();
        }
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static String DegreesToCardinal(double degrees)
    {
        String[] caridnals = { "North", "North East", "East", "South East", "South", "South West", "West", "North West", "North" };
        return caridnals[(int)Math.round(((double)degrees % 360) / 45)];
    }


    public static void speak(final String input){

// speak out string file function

        final Handler h =new Handler();

        Runnable r = new Runnable() {

            public void run() {

                // detect if the function still speaking previous request. if yes, it will delay current request.

                if (!t1.isSpeaking()) {
                    t1.speak(input, TextToSpeech.QUEUE_FLUSH, null);
                }else {
                    h.postDelayed(this, 1000);
                }
            }
        };

        h.postDelayed(r, 1000);




    }


    void configure_button(){
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        ,10);
            }
            speak("You do not have permission to access location informatin.");
mTextMessage.setText("You do not have permission to access location informatin.");
             return;
        }
// this code won't execute IF permissions are not allowed, because in the line above there is return statement.
                //noinspection MissingPermission
        speak("Accessing location information ....");
        mTextMessage.setText("Accessing location information ....");
        locationManager.requestLocationUpdates("gps", 30000, 0, listener);

    }

    void GeoLocationDecode(Location location){
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
String errorMessage = "";
        String TAG = "ERROR TAG";
        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    // In this sample, get just a single address.
                    1);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            speak("Geocoding Service Bot Available");
           errorMessage ="Geocoding Service Not Available";
            Log.e("ERROR TAG", errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            speak("invalid lat long used");
            errorMessage = "invalid lat long used";
            Log.e(TAG, errorMessage + ". " +
                    "Latitude = " + location.getLatitude() +
                    ", Longitude = " +
                    location.getLongitude(), illegalArgumentException);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size()  == 0) {
            if (errorMessage.isEmpty()) {
                speak("No Address found");
                errorMessage = "no address found";
                Log.e(TAG, errorMessage);
            }
        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            Log.i(TAG, "address found");


            if(global.action == "navigate") {

                Position wmi = Position.fromCoordinates(location.getLongitude(), location.getLatitude());

                if (global.startNavigate == true)
                {
                    global.origine = wmi;
                }

                getRoute(wmi, global.destination,addressFragments.toString());

                global.startNavigate = false;

            } if(global.action == "places0"){
                speak(addressFragments.toString());
                getPlaces(location);
            }if (global.action== "") {
                speak(addressFragments.toString());
                checkDistance(location);
                mTextMessage.append("\n " + addressFragments);

            }
        }


    }

    private void promptSpeechInput() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {

            startActivityForResult(intent, 100);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 100: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    global.recordname = result.get(0);

                    global.action = "confirmrecord2";
                    speak("Your record name is " + global.recordname + ". Tap Screen to confirm. Long press to speak again.");
                }
                break;
            }

        }
    }

    private void checkDistance(Location location){

        mydb = new DBhelper(mContext);
        global.dataRoute = new ArrayList<ArrayList<String>>();

        global.dataRoute = mydb.getAllLocationData();
        int i =0;
        int n = global.dataRoute.size();
        // final ArrayList<Position> points = new ArrayList<>();


        Position origin;
        Position destination;



        for (int j = 0;  j < n ; j++) {

            ArrayList<String> dataitem = global.dataRoute.get(j);
            String id = dataitem.get(0);
            String recordid = dataitem.get(1);
            String altitude = dataitem.get(2);
            String longtitude = dataitem.get(3);
            String timestamp = dataitem.get(4);

            float[] result = new float[1];

            Location.distanceBetween(location.getLatitude(),location.getLongitude(),Double.parseDouble(altitude),Double.parseDouble(longtitude),result);


            global.dataNearby = new ArrayList<String>();
            if (result[0] < 100){

                global.dataNearby.add(recordid);
            }

            Set<String> uniqueRecordid = new HashSet<String>(global.dataNearby);

            int m = uniqueRecordid.size();

            if (m>0){
                global.action = "nearby";
                speak("You have reached nearby record. You have " + String.valueOf(m) + " records. Tap screen if you wanna access the record data. Long press to cancel.");
            }

            Log.d(DEBUG_TAG, "Waypoint: " + altitude + " : " + longtitude);
        }

    }


    private void getRoute(Position origin, Position destination, final String locationstring) throws ServicesException {
        ArrayList<Position> positions = new ArrayList<>();
        positions.add(origin);
        positions.add(destination);

        MapboxDirections client = new MapboxDirections.Builder()
                .setAccessToken(Utils.getMapboxAccessToken(this))
                .setCoordinates(positions)
                .setProfile(DirectionsCriteria.PROFILE_WALKING)
                .setSteps(true)
                .setOverview(DirectionsCriteria.OVERVIEW_FULL)
                .build();

        client.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                // You can get generic HTTP info about the response
                Log.d("TAG", "Response code: " + response.code());
                if (response.body() == null) {
                    Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                    return;
                }

                // Print some info about the route
                currentRoute = response.body().getRoutes().get(0);
                Log.d("TAG", "Distance: " + currentRoute.getDistance());
                showMessage(String.format(Locale.US, "Route has %d steps and it's %.1f meters long.",
                        currentRoute.getLegs().get(0).getSteps().size(),
                        currentRoute.getDistance()));

                // Draw the route on the map
                String instruction = currentRoute.getLegs().get(0).getSteps().get(0).getManeuver().getInstruction().toString();
                String msg = "You are at " + locationstring + ". it's "+ String.valueOf(currentRoute.getDistance())+" meters away . " + instruction;
                speak(msg);
                mTextMessage.append("\n " + msg);

            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                Log.e("TAG", "Error: " + throwable.getMessage());
                showMessage("Error: " + throwable.getMessage());
            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void getPlaces(Location location){

        //double mLatitude=0;
        //double mLongitude=0;


        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available

            int requestCode = 10;
          GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            speak("Google map connection error");

            Log.d("URL","Google Play Service Util error");
          //  dialog.show();

        }else { // Google Play Services are available


        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        sb.append("location="+location.getLatitude()+","+location.getLongitude());
        sb.append("&radius=1000");
       // sb.append("&types="+type);
        sb.append("&sensor=true");
        sb.append("&key=AIzaSyCgzRcXbzclTYTKzgDEqNFteOLgNPMnEbc");

        Log.d("URL",sb.toString());
        // Creating a new non-ui thread task to download json data
        PlacesTask placesTask = new PlacesTask();

        // Invokes the "doInBackground()" method of the class PlaceTask
        placesTask.execute(sb.toString());

    }}

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("url: ", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }

        return data;
    }


private class PlacesTask extends AsyncTask<String, Integer, String> {

    String data = null;

    // Invoked by execute() method of this object
    @Override
    protected String doInBackground(String... url) {
        try{
            data = downloadUrl(url[0]);
        }catch(Exception e){
            Log.d("Background Task",e.toString());
        }
        return data;
    }

    // Executed after the complete execution of doInBackground() method
    @Override
    protected void onPostExecute(String result){
        ParserTask parserTask = new ParserTask();

        // Start parsing the Google places in JSON format
        // Invokes the "doInBackground()" method of the class ParseTask
        parserTask.execute(result);
    }

    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
protected List<HashMap<String,String>> doInBackground(String... jsonData) {

    List<HashMap<String, String>> places = null;
PlaceJSONParser placeJsonParser = new PlaceJSONParser();

try{
jObject = new JSONObject(jsonData[0]);

/** Getting the parsed data as a List construct */
places = placeJsonParser.parse(jObject);

}catch(Exception e){
Log.d("Exception",e.toString());
}
return places;
}

        // Executed after the complete execution of doInBackground() method
        @Override
protected void onPostExecute(List<HashMap<String,String>> list){

// Clears all the existing markers
//  mGoogleMap.clear()
//
if (list.size() >= 1){
                global.data = new ArrayList<ArrayList<String>>();



for(int i=0;i<list.size();i++){

// Creating a marker
MarkerOptions markerOptions = new MarkerOptions();


// Getting a place from the places list
HashMap<String, String> hmPlace = list.get(i);

// Getting latitude of the place
double lat = Double.parseDouble(hmPlace.get("lat"));

// Getting longitude of the place
double lng = Double.parseDouble(hmPlace.get("lng"));

// Getting name
String name = hmPlace.get("place_name");

// Getting vicinity
String vicinity = hmPlace.get("vicinity");

LatLng latLng = new LatLng(lat, lng);

    ArrayList<String> dataitem = new ArrayList<String>();

    dataitem.add(String.valueOf(i+1));
    dataitem.add(name);
    dataitem.add(String.valueOf(lng));
    dataitem.add(String.valueOf(lat));

    global.data.add(dataitem);
}
global.action="places";
                speak("Scroll to navigate data.");

            }
}
}}
}



