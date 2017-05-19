package com.ospit.heng.myguidedogsdane;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

public class GPStracker extends Service {
    public GPStracker() {
    }
    public static Context context;
    LocationManager lm;
    Thread triggerService;
    DBhelper mydb;


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    public int onStartCommand(Intent intent, int flags, int startId) {

       context = this;
Toast.makeText(this, "GPS tacking starting", Toast.LENGTH_SHORT).show();
        addLocationListener();

        return START_STICKY;
    }

    private void addLocationListener()
    {
        //adding location listener.

        triggerService = new Thread(new Runnable(){
            public void run(){
                try{
                    Looper.prepare();//Initialise the current thread as a looper.
                    //context = Context.LOCATION_SERVICE;
                    lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                    Criteria c = new Criteria();
                    c.setAccuracy(Criteria.ACCURACY_COARSE);

                    final String PROVIDER = lm.getBestProvider(c, true);

                    MyLocationListener myLocationListener = new MyLocationListener();


                    if (lm != null) {
                        if ( ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                            lm.requestLocationUpdates(PROVIDER, 30000, 0, myLocationListener);
                        }
                    }
                    lm.requestLocationUpdates(PROVIDER, 30000, 0, myLocationListener);

                    Log.d("LOC_SERVICE", "Service RUNNING!");
                    Looper.loop();
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }, "LocationThread");

        Time now = new Time();
        now.setToNow();
        String datestring = String.valueOf(now.year) + "-" + String.valueOf(now.month) + "-" + String.valueOf(now.monthDay) + " " + String.valueOf(now.hour) + ":" + String.valueOf(now.minute) + ":" + String.valueOf(now.second);
        global.recordid = datestring;

        triggerService.start();



    }

    public  void updateLocation(Location location)
    {
        //update location info to the Database.

        double latitude, longitude;

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        mydb = new DBhelper(this);
        Time now = new Time();
        now.setToNow();
        String datestring = String.valueOf(now.year) + "-" + String.valueOf(now.month) + "-" + String.valueOf(now.monthDay) + " " + String.valueOf(now.hour) + ":" + String.valueOf(now.minute) + ":" + String.valueOf(now.second);

        mydb.inserlocatioon(global.recordid,Double.toString(latitude),Double.toString(longitude),datestring);

    }

    class MyLocationListener implements LocationListener
    {

        @Override
        public void onLocationChanged(Location location)
        {
            // on location change event.

            double latitude, longitude;
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            global.locationString = Double.toString(latitude) + "-" + Double.toString(longitude);

            updateLocation(location);
            Log.v("log_tag", "location data updated : " + global.locationString);


        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras){


        }

        @Override
        public void onProviderDisabled(String provider){

        }

        @Override
        public void onProviderEnabled(String provider){

        }
    }


}

