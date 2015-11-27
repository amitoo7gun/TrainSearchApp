package com.example.amit.cube26;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.amit.cube26.data.AppDBContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * Created by amit on 7/16/2015.
 */
public class FetchJSONData extends AsyncTask<Void,Void,Void> {
    public final String LOG_TAG = FetchJSONData.class.getSimpleName();
    private final Context mContext;
    private String source_city;
    private String destination_city;
    private String departure_date;

    public FetchJSONData(Context context,String source, String destination,String dep_date) {
        mContext = context;
        source_city=source;
        destination_city=destination;
        departure_date=dep_date;

    }
    @Override
    protected Void doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String JsonStr = null;

            try {
                mContext.getContentResolver().delete(AppDBContract.TrainEntry.CONTENT_URI,null,null);
                final String BASE_URL =
                        "https://cube26-1337.0x10.info/trains?";
//                final String BASE_URL =
//                        "https://cube26-1337.0x10.info/trains?source=BHUBANESWAR&destination=VISAKHAPATNAM";
//
//                final String FORECAST_BASE_URL =
//                        "http://api.openweathermap.org/data/2.5/forecast/daily?";
                final String QUERY_PARAM1 = "source";
                final String QUERY_PARAM2 = "destination";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM1, source_city)
                        .appendQueryParameter(QUERY_PARAM2, destination_city)
                        .build();

                URL url = new URL(builtUri.toString());
                Log.d("URI Final:::",builtUri.toString());

//                Uri builtUri = Uri.parse(BASE_URL).buildUpon().build();

//                URL url = new URL(builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                //InputStream inputStream = getClass().getResourceAsStream("game_data.json");
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.

                }
                JsonStr = buffer.toString();
                getDataFromJson(JsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

        return null;
    }



    private void getDataFromJson(String JsonStr)
            throws JSONException {

        //Log.v(LOG_TAG,JsonStr);
        // Now we have a String representing the complete forecast in JSON Format.
        // Fortunately parsing is easy:  constructor takes the JSON string and converts it
        // into an Object hierarchy for us.

        // These are the names of the JSON objects that need to be extracted.

        // Location information
        final String TRAIN_NAME = "trainName";
        final String TRAIN_NUMBER = "trainNo";
        final String TRAIN_SOURCE_STATION_CODE = "sourceStationCode";
        final String TRAIN_SOURCE_STATION_NAME = "sourceStationName";
        final String TRAIN_ARRIVAL_TIME = "arrivalTime";
        final String TRAIN_DEPARTURE_TIME = "departureTime";
        final String TRAIN_DISTANCE = "distance";
        final String TRAIN_DESTINATION_STATION_CODE = "destinationStationCode";
        final String TRAIN_DESTINATION_STATION_NAME = "destinationStationName";


        try {
            JSONArray trainsJsonArray = new JSONArray(JsonStr);
            Vector<ContentValues> cVVector = new Vector<ContentValues>(trainsJsonArray.length());
            for(int i=0;i<trainsJsonArray.length();i++)
            {
                JSONObject trainDetailsJson = trainsJsonArray.getJSONObject(i);
                String train_name = trainDetailsJson.getString(TRAIN_NAME);
                String train_number = trainDetailsJson.getString(TRAIN_NUMBER);
                String train_src_stn_code = trainDetailsJson.getString(TRAIN_SOURCE_STATION_CODE);
                String train_src_stn_name = trainDetailsJson.getString(TRAIN_SOURCE_STATION_NAME);
                String train_arr_time = trainDetailsJson.getString(TRAIN_ARRIVAL_TIME);
                String train_dep_time = trainDetailsJson.getString(TRAIN_DEPARTURE_TIME);
                String train_distance = trainDetailsJson.getString(TRAIN_DISTANCE);
                String train_dest_stn_code = trainDetailsJson.getString(TRAIN_DESTINATION_STATION_CODE);
                String train_dest_stn_name = trainDetailsJson.getString(TRAIN_DESTINATION_STATION_NAME);



                Log.v(LOG_TAG,train_name);
                ContentValues productsValues = new ContentValues();
                productsValues.put(AppDBContract.TrainEntry.COLUMN_TRAIN_NAME, train_name);
                productsValues.put(AppDBContract.TrainEntry.COLUMN_TRAIN_NUMBER, train_number);
                productsValues.put(AppDBContract.TrainEntry.COLUMN_TRAIN_SOURCE_STATION_CODE, train_src_stn_code);
                productsValues.put(AppDBContract.TrainEntry.COLUMN_TRAIN_SOURCE_STATION_NAME, train_src_stn_name);
                productsValues.put(AppDBContract.TrainEntry.COLUMN_TRAIN_ARRIVAL_TIME, train_arr_time);
                productsValues.put(AppDBContract.TrainEntry.COLUMN_TRAIN_DEPARTURE_TIME, train_dep_time);
                productsValues.put(AppDBContract.TrainEntry.COLUMN_TRAIN_DISTANCE, train_distance);
                productsValues.put(AppDBContract.TrainEntry.COLUMN_TRAIN_DESTINATION_STATION_CODE, train_dest_stn_code);
                productsValues.put(AppDBContract.TrainEntry.COLUMN_TRAIN_DESTINATION_STATION_NAME, train_dest_stn_name);

                cVVector.add(productsValues);
            }

            int inserted = 0;
            // add to database
            if ( cVVector.size() > 0 ) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);

                mContext.getContentResolver().bulkInsert(AppDBContract.TrainEntry.CONTENT_URI, cvArray);


            }

            Log.d(LOG_TAG, "Sync Complete. " + cVVector.size() + " Inserted");

        }
        catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }
}
