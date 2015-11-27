package com.example.amit.cube26.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
* Created by amit on 7/20/2015.
*/
public class AppDBHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 3;

    static final String DATABASE_NAME = "cube26.db";

    public AppDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold locations.  A location consists of the string supplied in the
        // location setting, the city name, and the latitude and longitude
        final String SQL_CREATE_TRAINS_TABLE = "CREATE TABLE " + AppDBContract.TrainEntry.TABLE_NAME + " (" +
                AppDBContract.TrainEntry._ID + " INTEGER PRIMARY KEY," +
                AppDBContract.TrainEntry.COLUMN_TRAIN_NAME + " TEXT NOT NULL, " +
                AppDBContract.TrainEntry.COLUMN_TRAIN_NUMBER + " TEXT NOT NULL, " +
                AppDBContract.TrainEntry.COLUMN_TRAIN_SOURCE_STATION_CODE + " TEXT NOT NULL, " +
                AppDBContract.TrainEntry.COLUMN_TRAIN_SOURCE_STATION_NAME + " TEXT NOT NULL, " +
                AppDBContract.TrainEntry.COLUMN_TRAIN_ARRIVAL_TIME + " TEXT NOT NULL, " +
                AppDBContract.TrainEntry.COLUMN_TRAIN_DEPARTURE_TIME + " TEXT NOT NULL, " +
                AppDBContract.TrainEntry.COLUMN_TRAIN_DISTANCE + " TEXT NOT NULL, " +
                AppDBContract.TrainEntry.COLUMN_TRAIN_DESTINATION_STATION_CODE + " TEXT NOT NULL, " +
                AppDBContract.TrainEntry.COLUMN_TRAIN_DESTINATION_STATION_NAME + " TEXT NOT NULL " +

                " );";


        final String SQL_CREATE_CITY_TABLE = "CREATE TABLE " + AppDBContract.CitiesEntry.TABLE_NAME + " (" +
                AppDBContract.CitiesEntry._ID + " INTEGER PRIMARY KEY," +
                AppDBContract.CitiesEntry.COLUMN_CITY_NAME + " TEXT NOT NULL, " +
                " UNIQUE (" + AppDBContract.CitiesEntry.COLUMN_CITY_NAME + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_TRAINS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_CITY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + AppDBContract.TrainEntry.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }
}
