package com.ospit.heng.myguidedogsdane;

import android.net.Uri;

import com.mapbox.services.commons.models.Position;

import java.util.ArrayList;

/**
  * Implemented by Kok Heng on 03/03/2017
  * realter on 03/04/2017
  */

// global variable to use across different instant.

public class global {
    public static String username; // the username
    public static String locationString; // location name
    public static String recordid; // record id string
    public static String action = ""; // initial the intial string of null
    public static String recordname = ""; // initial record name
    public static ArrayList<ArrayList<String>> data; // array list of the data
    public static ArrayList<String> dataNearby; // array list of surrounding data name
    public static ArrayList<ArrayList<String>> dataRoute; // array list of route data
    public static int selectedIndex =0;
    public static int datasize; // the size of the data
    public static String sRecordName = ""; // the record route name
    public static String sRecordID = ""; // the recorded ID name
    public static Position origine; // from point of origin
    public static Position destination; // the position of the destination
    public static boolean startNavigate = false; // return a success starting navigation
    public static boolean compassctrl = false; // return a success of compass mode of the position
    public static ArrayList<String> rotation;
}
