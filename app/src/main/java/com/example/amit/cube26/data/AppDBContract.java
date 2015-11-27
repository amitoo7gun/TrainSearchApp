package com.example.amit.cube26.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;


/**
* Created by amit on 7/20/2015.
*/
public class AppDBContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.example.amit.cube26";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
    // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_CUBE26 = "train_details";
    public static final String PATH_CITIES = "cities";



    /* Inner class that defines the table contents of the products table */
    public static final class TrainEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CUBE26).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CUBE26;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CUBE26;

        public static final String TABLE_NAME = "trains";


        public static final String COLUMN_TRAIN_NAME = "name";
        public static final String COLUMN_TRAIN_NUMBER = "train_number";
        public static final String COLUMN_TRAIN_SOURCE_STATION_CODE = "src_station_code";
        public static final String COLUMN_TRAIN_SOURCE_STATION_NAME = "src_station_name";
        public static final String COLUMN_TRAIN_ARRIVAL_TIME = "arrival_time";
        public static final String COLUMN_TRAIN_DEPARTURE_TIME = "departure_time";
        public static final String COLUMN_TRAIN_DISTANCE = "distance";
        public static final String COLUMN_TRAIN_DESTINATION_STATION_CODE = "dest_station_code";
        public static final String COLUMN_TRAIN_DESTINATION_STATION_NAME = "dest_station_name";



        public static Uri buildTrainsDetail(int trainID) {
            return CONTENT_URI.buildUpon().appendPath(Integer.toString(trainID)).build();
        }

        public static String getParcelIDIDFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

    }

    public static final class CitiesEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CITIES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CITIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CITIES;

        public static final String TABLE_NAME = "cities";


        public static final String COLUMN_CITY_NAME = "city";


//
//        public static Uri buildFaasosDetail(int faasosID) {
//            return CONTENT_URI.buildUpon().appendPath(Integer.toString(faasosID)).build();
//        }
//
//        public static String getParcelIDIDFromUri(Uri uri) {
//            return uri.getPathSegments().get(1);
//        }

    }

}
