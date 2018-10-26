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
    public static String username; // user name
    public static String locationString; // location name 
    public static String recordid; // the record ID
    public static String action = ""; // the action name
    public static String recordname = ""; // the record route name
    public static ArrayList<ArrayList<String>> data; // array list of the data name
    public static ArrayList<String> dataNearby; // array list of the nearby data name
    public static ArrayList<ArrayList<String>> dataRoute; // array list of route name
    public static int selectedIndex =0; // initial the list as begin
    public static int datasize; // the size of data
    public static String sRecordName = ""; // recorded name
    public static String sRecordID = ""; // the recording id name
    public static Position origine; // the origin place name
    public static Position destination; // the destination position
    public static boolean startNavigate = false; // return starting navigate
    public static boolean compassctrl = false; // determine the compass mode
    public static ArrayList<String> rotation;
}
