package com.example.amit.cube26;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

public class CitySelectionActivity extends AppCompatActivity implements View.OnClickListener{


//    public static SharedPreferences sp;
//    String source=null;
//    String destination=null;
    TextView mSourceTextView;
    TextView mDestinationTextView;
    Button mSearchButton;
    DatePicker mDdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_selection);
        mSourceTextView=(TextView)findViewById(R.id.source);
        mDestinationTextView=(TextView)findViewById(R.id.destination);
        mSearchButton=(Button)findViewById(R.id.search_btn);
        mDdate=(DatePicker)findViewById(R.id.departure_datePicker);

        mSearchButton.setOnClickListener(this);
        mSourceTextView.setOnClickListener(this);
        mDestinationTextView.setOnClickListener(this);

//        sp=this.getSharedPreferences("service_validation", MODE_WORLD_READABLE);
//        source=sp.getString("SOURCE", source);
//        destination=sp.getString("DESTINATION", destination);
//        Log.d("Source is:::", "is" + source);
//        Log.d("Destination is:::", "is" + destination);

       mSourceTextView.setText("Source");
       mDestinationTextView.setText("Destination");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_city_selection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onClick(View v){
        switch (v.getId()) {
            case R.id.source:
            {
                Intent i = new Intent(this, SearchActivity.class);
                startActivityForResult(i, 1);
                break;
            }
            case R.id.destination:
            {
                Intent j = new Intent(this, DestinationSearchActivity.class);
                startActivityForResult(j, 2);
                break;
            }
            case R.id.search_btn:
            {
                if( mSourceTextView.getText()!= "Source" && mDestinationTextView.getText()!="Destination" && ( mSourceTextView.getText()!= mDestinationTextView.getText())) {
                    Intent k = new Intent(this, MainActivity.class);
                    k.putExtra("SOURCE", mSourceTextView.getText());
                    k.putExtra("DESTINATION", mDestinationTextView.getText());
                    k.putExtra("DDATE", mDdate.getDayOfMonth() + "/" + mDdate.getMonth() + "/" + mDdate.getYear());
                    startActivity(k);
                }
                else
                {
                    new AlertDialog.Builder(this)
                            .setTitle("Chosse City")
                            .setMessage("Please select a Source and Destination to proceed!")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            }).setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                break;
            }


        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                mSourceTextView.setText(result);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
        if (requestCode == 2) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                mDestinationTextView.setText(result);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}
