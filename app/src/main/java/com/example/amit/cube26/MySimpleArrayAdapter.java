package com.example.amit.cube26;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amit.cube26.dummy.DummyContent;

import java.util.List;

/**
 * Created by amit on 10/15/2015.
 */
    public class MySimpleArrayAdapter extends BaseAdapter{
        private final Context context;
        private final List<DummyContent.DummyItem> values;

        public MySimpleArrayAdapter(Context context, List<DummyContent.DummyItem> values) {
            this.context = context;
            this.values = values;

        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.jobsrow, parent, false);
            TextView jobnametextView = (TextView) rowView.findViewById(R.id.joblabel);
            TextView jobStarttimetextView = (TextView) rowView.findViewById(R.id.starttime);
            TextView jobEndtimetextView = (TextView) rowView.findViewById(R.id.endtime);
            TextView jobRunningtimetextView = (TextView) rowView.findViewById(R.id.runtime);
            TextView jobRunningCycletextView = (TextView) rowView.findViewById(R.id.runcycle);

            jobnametextView.setText(DummyContent.ITEMS.get(position).Jobname);
            jobStarttimetextView.setText("Start time: "+DummyContent.ITEMS.get(position).Startdate);
            jobEndtimetextView.setText("End time: "+DummyContent.ITEMS.get(position).Enddate);
            jobRunningtimetextView.setText("Duration: "+DummyContent.ITEMS.get(position).Runtime);
            jobRunningCycletextView.setText(DummyContent.ITEMS.get(position).RunningCycle);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

            if (DummyContent.ITEMS.get(position).status==1)
            {
                imageView.setImageResource(R.drawable.greendot);
            } else {
                imageView.setImageResource(R.drawable.reddot);
            }

            return rowView;
        }

    @Override
    public int getCount() {
        return DummyContent.ITEMS.size();
    }

    @Override
    public DummyContent.DummyItem getItem(int position) {
        return DummyContent.ITEMS.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
