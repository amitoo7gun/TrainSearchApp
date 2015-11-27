package com.example.amit.cube26;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.amit.cube26.data.AppDBContract;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * Created by amit on 10/4/2015.
 */
public class FetchCityNames extends AsyncTask<Void,Void,Void> {
    public final String LOG_TAG = FetchJSONData.class.getSimpleName();
    private final Context mContext;

    public FetchCityNames(Context context) {
        mContext = context;

    }
    @Override
    protected Void doInBackground(Void... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String JsonStr = null;

        try {

            final String BASE_URL =
                    "https://cube26-1337.0x10.info/stations";

            Uri builtUri = Uri.parse(BASE_URL).buildUpon().build();

            URL url = new URL(builtUri.toString());

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


        try {
            JSONArray cityJsonArray = new JSONArray(JsonStr);
            Vector<ContentValues> cVVector = new Vector<ContentValues>(cityJsonArray.length());
            for(int i=0;i<cityJsonArray.length();i++)
            {
                String city_name = cityJsonArray.optString(i);




                Log.v(LOG_TAG,city_name);
                ContentValues cityValues = new ContentValues();
                cityValues.put(AppDBContract.CitiesEntry.COLUMN_CITY_NAME, city_name);


                cVVector.add(cityValues);
            }

            int inserted = 0;
            // add to database
            if ( cVVector.size() > 0 ) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);

                mContext.getContentResolver().bulkInsert(AppDBContract.CitiesEntry.CONTENT_URI, cvArray);


            }

            Log.d(LOG_TAG, "Sync Complete. " + cVVector.size() + "Cities Inserted");

        }
        catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }
}
