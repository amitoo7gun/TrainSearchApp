package com.example.amit.cube26;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.amit.cube26.data.AppDBContract;

/**
 * Created by amit on 7/27/2015.
 */
public class DetailFragment extends android.support.v4.app.Fragment implements LoaderManager.LoaderCallbacks<Cursor>,View.OnClickListener {
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    static final String DETAIL_URI = "URI";

    private static final String APP_SHARE_HASHTAG = "#Cube26App";

    private ShareActionProvider mShareActionProvider;
    private String mTrainShare;
    private Uri mUri;



    private static final int DETAIL_LOADER = 0;

    private static final String[] DETAIL_COLUMNS = {
            AppDBContract.TrainEntry._ID,
            AppDBContract.TrainEntry.COLUMN_TRAIN_NAME,
            AppDBContract.TrainEntry.COLUMN_TRAIN_NUMBER,
            AppDBContract.TrainEntry.COLUMN_TRAIN_SOURCE_STATION_NAME,
            AppDBContract.TrainEntry.COLUMN_TRAIN_SOURCE_STATION_CODE,
            AppDBContract.TrainEntry.COLUMN_TRAIN_DESTINATION_STATION_NAME,
            AppDBContract.TrainEntry.COLUMN_TRAIN_DESTINATION_STATION_CODE,
            AppDBContract.TrainEntry.COLUMN_TRAIN_ARRIVAL_TIME,
            AppDBContract.TrainEntry.COLUMN_TRAIN_DEPARTURE_TIME,
            AppDBContract.TrainEntry.COLUMN_TRAIN_DISTANCE

    };


    public static final int COL_TRAIN_ID = 0;
    public static final int COL_TRAIN_NAME = 1;
    public static final int COL_TRAIN_NUMBER = 2;
    public static final int COL_TRAIN_SOURCE_NAME = 3;
    public static final int COL_TRAIN_SOURCE_CODE = 4;
    public static final int COL_TRAIN_DESTINATION_NAME = 5;
    public static final int COL_TRAIN_DESTINATION_CODE = 6;
    public static final int COL_TRAIN_ARRIVAL_TIME = 7;
    public static final int COL_TRAIN_DEPARTURE_TIME = 8;
    public static final int COL_TRAIN_DISTANCE = 9;







    private TextView mNameView;
    private TextView mNumberView;
    private TextView mSourceNameView;
    private TextView mSourceCodeView;
    private TextView mDestNameView;
    private TextView mDestCodeView;
    private TextView mArrivalView;
    private TextView mDepartureView;
    private TextView mDistanceView;


    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void loadmap();
    }
    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mNameView = (TextView) rootView.findViewById(R.id.textView1);
        mNumberView = (TextView) rootView.findViewById(R.id.textView2);
        mSourceNameView = (TextView) rootView.findViewById(R.id.textView3);
//        mSourceCodeView = (TextView) rootView.findViewById(R.id.textView4);
        mDestNameView = (TextView) rootView.findViewById(R.id.textView5);
//        mDestCodeView = (TextView) rootView.findViewById(R.id.textView6);
        mArrivalView = (TextView) rootView.findViewById(R.id.textView7);
        mDepartureView = (TextView) rootView.findViewById(R.id.textView8);
        mDistanceView = (TextView) rootView.findViewById(R.id.textView9);


//        mLikedView.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        //do what you want to do when button is clicked
        switch (v.getId()) {
//            case R.id.detail_itemlike_textview:
//             {
////                ContentValues updateValues= new ContentValues();
////                updateValues.put(AppDBContract.TrainEntry.COLUMN_FAASOS_LIKED, "1");
////                String whereCl= AppDBContract.TrainEntry.COLUMN_FAASOS_NAME + "= \"" + mNameView.getText()+ "\"";
////                getContext().getContentResolver().update(AppDBContract.TrainEntry.CONTENT_URI,updateValues,whereCl,null);
////                mLikedView.setText("Liked :)");
////                break;
//            }


        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.detailfragment, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // If onLoadFinished happens before this, we can go ahead and set the share intent now.
        if (mTrainShare != null) {
            mShareActionProvider.setShareIntent(createShareParcelIntent());
        }
    }

    private Intent createShareParcelIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mTrainShare + APP_SHARE_HASHTAG);
        return shareIntent;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if ( null != mUri ) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    DETAIL_COLUMNS,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {



            mNameView.setText(data.getString(COL_TRAIN_NAME));
            mNumberView.setText("Train Number: "+data.getString(COL_TRAIN_NUMBER));
            mSourceNameView.setText("From: "+data.getString(COL_TRAIN_SOURCE_NAME));
//            mSourceCodeView.setText(data.getString(COL_TRAIN_SOURCE_CODE));
            mDestNameView.setText("To: "+data.getString(COL_TRAIN_DESTINATION_NAME));
//            mDestCodeView.setText(data.getString(COL_TRAIN_DESTINATION_CODE));
            mArrivalView.setText("Arrives at: "+data.getString(COL_TRAIN_ARRIVAL_TIME));
            mDepartureView.setText("Departs at: "+data.getString(COL_TRAIN_DEPARTURE_TIME));
            mDistanceView.setText("Total distance: "+data.getString(COL_TRAIN_DISTANCE));



            mTrainShare= String.format("Train %s , Train no.%s - from: %s -to: %s , Departure time: %s", data.getString(COL_TRAIN_NAME), data.getString(COL_TRAIN_NUMBER)
            ,data.getString(COL_TRAIN_SOURCE_NAME),data.getString(COL_TRAIN_DESTINATION_NAME),data.getString(COL_TRAIN_DEPARTURE_TIME));
            // If onCreateOptionsMenu has already happened, we need to update the share intent now.
            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(createShareParcelIntent());
            }
        }

    }



    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }


}
