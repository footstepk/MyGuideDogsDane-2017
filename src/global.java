package com.ospit.heng.myguidedogsdane;

import android.net.Uri;

import com.mapbox.services.commons.models.Position;

import java.util.ArrayList;

// global variable to use across different instant.

public class global {
    public static String username;
    public static String locationString;
    public static String recordid;
    public static String action = "";
    public static String recordname = "";
    public static ArrayList<ArrayList<String>> data;
    public static ArrayList<String> dataNearby;
    public static ArrayList<ArrayList<String>> dataRoute;
    public static int selectedIndex =0;
    public static int datasize;
    public static String sRecordName = "";
    public static String sRecordID = "";
    public static Position origine;
    public static Position destination;
    public static boolean startNavigate = false;
    public static boolean compassctrl = false;
    public static ArrayList<String> rotation;
}
