package com.example.amit.cube26.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
* Created by amit on 7/20/2015.
*/
public class AppProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private AppDBHelper mOpenHelper;

    static final int CUBE26 = 100;
    static final int CUBE26_WITH_ID = 101;
    static final int CITIES = 102;


    private static final SQLiteQueryBuilder sProductsQueryBuilder;

    static{
        sProductsQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //weather INNER JOIN location ON weather.location_id = location._id
        sProductsQueryBuilder.setTables( AppDBContract.TrainEntry.TABLE_NAME );


    }

    //_ID = ?
    private static final String sCube26IDSelection =
            AppDBContract.TrainEntry.TABLE_NAME +
                    "." + AppDBContract.TrainEntry._ID + " = ? ";


    private Cursor getParcelsDetails(Uri uri, String[] projection, String sortOrder) {
        String parcelID = AppDBContract.TrainEntry.getParcelIDIDFromUri(uri);

        String[] selectionArgs;
        String selection;


            selection = sCube26IDSelection;
            selectionArgs = new String[]{parcelID};


        return sProductsQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }


    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = AppDBContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, AppDBContract.PATH_CUBE26, CUBE26);
        matcher.addURI(authority, AppDBContract.PATH_CUBE26 + "/*", CUBE26_WITH_ID);
        matcher.addURI(authority, AppDBContract.PATH_CITIES, CITIES);
        return matcher;
    }

    /*
        Students: We've coded this for you.  We just create a new WeatherDbHelper for later use
        here.
     */
    @Override
    public boolean onCreate() {
        mOpenHelper = new AppDBHelper(getContext());
        return true;
    }

    /*
        Students: Here's where you'll code the getType function that uses the UriMatcher.  You can
        test this by uncommenting testGetType in TestProvider.

     */
    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            // Student: Uncomment and fill out these two cases

            case CUBE26_WITH_ID:
                return AppDBContract.TrainEntry.CONTENT_TYPE;
            case CUBE26:
                return AppDBContract.TrainEntry.CONTENT_TYPE;
            case CITIES:
                return AppDBContract.CitiesEntry.CONTENT_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {

            // "weather/*"
            case CUBE26_WITH_ID: {
                retCursor = getParcelsDetails(uri, projection, sortOrder);
                break;
            }
            // "weather"
            case CUBE26: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        AppDBContract.TrainEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case CITIES: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        AppDBContract.CitiesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    /*
        Student: Add the ability to insert Locations to the implementation of this function.
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case CUBE26: {
                long _id = db.insert(AppDBContract.TrainEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = ContentUris.withAppendedId(AppDBContract.TrainEntry.CONTENT_URI, _id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case CITIES: {
                long _id = db.insert(AppDBContract.CitiesEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = ContentUris.withAppendedId(AppDBContract.CitiesEntry.CONTENT_URI, _id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case CUBE26:
                rowsDeleted = db.delete(
                        AppDBContract.TrainEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }



    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case CUBE26:

                rowsUpdated = db.update(AppDBContract.TrainEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CUBE26: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(AppDBContract.TrainEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case CITIES: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(AppDBContract.CitiesEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            default:
                return super.bulkInsert(uri, values);
        }
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
