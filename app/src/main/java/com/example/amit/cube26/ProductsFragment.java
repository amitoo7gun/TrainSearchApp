package com.example.amit.cube26;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.amit.cube26.data.AppDBContract;
import com.example.amit.cube26.sync.AppSyncAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProductsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    public static final String LOG_TAG = ProductsFragment.class.getSimpleName();
    private ProductsAdapter mTrainAdapter;

    private ListView mListView;
    private TextView mApiHitView;
    private TextView mTotal_itemView;
    private Switch VegSwitch;

    public static SharedPreferences sp;
    int item_count=0;

    private int mPosition = ListView.INVALID_POSITION;

    private static final String SELECTED_KEY = "selected_position";
// SQL query Pareameters
    public static String sortOrder=null;
    public static String whereClause=null;
    public static String whereArgs=null;


    private static final int TRAIN_LOADER = 0;
    // For the forecast view we're showing only a small subset of the stored data.
    // Specify the columns we need.
    private static final String[] TRAINS_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.

            AppDBContract.TrainEntry._ID,
            AppDBContract.TrainEntry.COLUMN_TRAIN_NAME,
            AppDBContract.TrainEntry.COLUMN_TRAIN_ARRIVAL_TIME,
            AppDBContract.TrainEntry.COLUMN_TRAIN_DEPARTURE_TIME
    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    static final int COL_TRAIN_ID = 0;
    static final int COL_TRAIN_NAME = 1;
    static final int COL_TRAIN_ARRIVAL = 2;
    static final int COL_TRAIN_DEPARTURE = 3;



    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Uri dateUri);
    }

    public ProductsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.products_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.menuSortName:
                updateProductsOrder(1);
                return true;
            case R.id.menuSortArrival:
                updateProductsOrder(-1);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // The ForecastAdapter will take data from a source and
        // use it to populate the ListView it's attached to.
        mTrainAdapter = new ProductsAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        mListView = (ListView) rootView.findViewById(R.id.listview_products);
        mApiHitView = (TextView) rootView.findViewById(R.id.api_hits_textview);
        mTotal_itemView= (TextView) rootView.findViewById(R.id.total_items_textview);
        mListView.setAdapter(mTrainAdapter);

        sp=this.getActivity().getSharedPreferences("service_validation", Context.MODE_WORLD_READABLE);
        item_count=sp.getInt("TOTAL_ITEMS", item_count);
        mTotal_itemView.setText("Menu Item: " + "13");

        new getAPIHits().execute("https://cube26-1337.0x10.info/hits");


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    ((Callback) getActivity())
                            .onItemSelected(AppDBContract.TrainEntry.buildTrainsDetail(
                                    cursor.getInt(COL_TRAIN_ID)
                            ));
                }
                mPosition = position;
            }
        });


        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {

            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }


        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(TRAIN_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    void RestartLoaderFunc(){
        getLoaderManager().restartLoader(TRAIN_LOADER, null, this);
    }

    // since we read the location when we create the loader, all we need to do is restart things
    void updateProductsOrder(int order ) {
        if(order == 1)
            sortOrder= AppDBContract.TrainEntry.COLUMN_TRAIN_NAME+ " ASC";
        else if(order == -1)
            sortOrder= AppDBContract.TrainEntry.COLUMN_TRAIN_ARRIVAL_TIME+ " ASC";

        getLoaderManager().restartLoader(TRAIN_LOADER, null, this);

    }



    private void updateProducts() {
        AppSyncAdapter.syncImmediately(getActivity());
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
        // so check for that before storing.
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {



    return new CursorLoader(getActivity(),
            AppDBContract.TrainEntry.CONTENT_URI,
            TRAINS_COLUMNS,
            whereClause,
            null,
            sortOrder);


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mTrainAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mListView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mTrainAdapter.swapCursor(null);
    }


//Async task to get API Hit Count

    private class getAPIHits extends AsyncTask<String, String, String> {



        protected String doInBackground(String... urls) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String JsonStr = null;
            String BASE_URL = urls[0];
            String apihitcount = null;
            try {

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
                apihitcount = getDataFromJson(JsonStr);
            }  catch (IOException e) {
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
            return apihitcount;
        }


        private String getDataFromJson(String JsonStr)
                throws JSONException {


            final String API_NAME = "api_hits";
            String apicount=null;

            try {
                JSONObject faasosJsonMainObject = new JSONObject(JsonStr);
                apicount= faasosJsonMainObject.getString(API_NAME);
            }
            catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return apicount;
        }
        protected void onPostExecute(String result) {
            mApiHitView.setText("API Hits: "+result);
        }
    }

}
