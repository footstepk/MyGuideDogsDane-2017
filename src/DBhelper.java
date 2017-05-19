package com.ospit.heng.myguidedogsdane;

/**
 * Created by alexcheong on 18/07/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DBhelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "myguidedogsdane.db";
    public static final String CONTACTS_TABLE_NAME = "locationdata";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_STATUS = "altitude";
    public static final String CONTACTS_COLUMN_IMAGE = "longtitude";
    public static final String CONTACTS_COLUMN_DATETIME = "datetime";
    private HashMap hp;

    public DBhelper(Context context)
    {
        super(context, DATABASE_NAME , null, 6);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table locationdata " +
                        "(id text, recordid text, altitude text,longtitude text,datetime text)"
        );

        db.execSQL(
                "create table locationFile " +
                        "(id text, recordname text, recordid text)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS locationdata");
        db.execSQL("DROP TABLE IF EXISTS locationFile");

        onCreate(db);
    }


    public boolean inserlocatioon (String recordid, String altitude, String longtitude, String timestamp)
    {

        //insert location waypoint information
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("recordid", recordid);
        contentValues.put("altitude", altitude);
        contentValues.put("longtitude", longtitude);
        contentValues.put("datetime", timestamp);

        db.insert("locationdata", null, contentValues);

        return true;
    }


    public boolean inserLocationName (String recordid, String recordname)
    {
        //insert record name that link with waypoint information

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("recordid", recordid);
        contentValues.put("recordname", recordname);

        db.insert("locationFile", null, contentValues);

        return true;
    }

    public Cursor getData(String id){

        //get location data. cursor info.
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from locationdata where recordid="+id+"", null );
        return res;
    }



    public int numberOfRowsTask(){

        // retreive number of data row of location data.

        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, "locationdata");
        return numRows;
    }


    public void clearLocationdata()
    {
        // clearing all the location data from db. For DB sanitizing purpose.

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM locationdata");
    }

    public void deleteLocationdata(String recordid)
    {
        // delete location data that link with record.

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM locationdata where recordid = '" + recordid + "'");
    }


    public ArrayList<ArrayList<String>> getLocationData(String ID)
        {

            //this function return a list of location data in Array.

            ArrayList<ArrayList<String>> array_list = new ArrayList<ArrayList<String>>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from locationdata where recordid ='"+ID + "' order by datetime", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            ArrayList<String> arrayitem = new ArrayList<String>();
            arrayitem.add(res.getString(res.getColumnIndex("id")));
            arrayitem.add(res.getString(res.getColumnIndex("recordid")));
            arrayitem.add(res.getString(res.getColumnIndex("altitude")));
            arrayitem.add(res.getString(res.getColumnIndex("longtitude")));
            arrayitem.add(res.getString(res.getColumnIndex("datetime")));

            array_list.add(arrayitem);
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<ArrayList<String>> getAllLocationData()
    {

        //return all the location data in an array.

        ArrayList<ArrayList<String>> array_list = new ArrayList<ArrayList<String>>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from locationdata order by datetime", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            ArrayList<String> arrayitem = new ArrayList<String>();
            arrayitem.add(res.getString(res.getColumnIndex("id")));
            arrayitem.add(res.getString(res.getColumnIndex("recordid")));
            arrayitem.add(res.getString(res.getColumnIndex("altitude")));
            arrayitem.add(res.getString(res.getColumnIndex("longtitude")));
            arrayitem.add(res.getString(res.getColumnIndex("datetime")));

            array_list.add(arrayitem);
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<ArrayList<String>> getLocationFile()
    {
        // return all the location record information.

        ArrayList<ArrayList<String>> array_list = new ArrayList<ArrayList<String>>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from locationFile order by recordid", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            ArrayList<String> arrayitem = new ArrayList<String>();
            arrayitem.add(res.getString(res.getColumnIndex("id")));
            arrayitem.add(res.getString(res.getColumnIndex("recordname")));
            arrayitem.add(res.getString(res.getColumnIndex("recordid")));

            array_list.add(arrayitem);
            res.moveToNext();
        }
        return array_list;
    }


    public ArrayList<String> getLocationFileByID(String recordid)
    {

        // get specific location file by recordid.

        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from locationFile where recordid = '"+ recordid +"' order by recordid", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            //ArrayList<String> arrayitem = new ArrayList<String>();
            array_list.add(res.getString(res.getColumnIndex("id")));
            array_list.add(res.getString(res.getColumnIndex("recordname")));
            array_list.add(res.getString(res.getColumnIndex("recordid")));

            res.moveToNext();
        }
        return array_list;
    }

    }
