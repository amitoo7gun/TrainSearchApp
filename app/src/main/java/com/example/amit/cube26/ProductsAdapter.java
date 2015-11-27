package com.example.amit.cube26;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by amit on 7/26/2015.
 */
public class ProductsAdapter extends CursorAdapter{


    // Flag to determine if we want to use a separate view for "today".
    private boolean mUseTodayLayout = true;

    /**
     * Cache of the children views for a forecast list item.
     */
    public static class ViewHolder {

        public final TextView nameView;
        public final TextView arrivalView;
        public final TextView departView;


        public ViewHolder(View view) {
            nameView = (TextView) view.findViewById(R.id.list_item_name_textview);
            departView = (TextView) view.findViewById(R.id.list_item_description_textview);
            arrivalView = (TextView) view.findViewById(R.id.list_item_spicemeter_textview);

        }
    }

    public ProductsAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Choose the layout type
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;
        layoutId = R.layout.list_item_forecast;

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        int viewType = getItemViewType(cursor.getPosition());

        String name = cursor.getString(ProductsFragment.COL_TRAIN_NAME);
        // Find TextView and set weather forecast on it
        viewHolder.nameView.setText(name);
        String arrival = cursor.getString(ProductsFragment.COL_TRAIN_ARRIVAL);
        // Find TextView and set weather forecast on it
        viewHolder.departView.setText("Departs at "+cursor.getString(ProductsFragment.COL_TRAIN_DEPARTURE));
        viewHolder.arrivalView.setText("Arrives at "+arrival);


    }

}
