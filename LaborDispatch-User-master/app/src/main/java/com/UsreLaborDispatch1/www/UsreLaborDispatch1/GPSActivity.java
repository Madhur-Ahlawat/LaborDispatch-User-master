package com.UsreLaborDispatch1.www.UsreLaborDispatch1;

import android.database.Cursor;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.UsreLaborDispatch1.www.sync.data.JobLocation;
import com.google.android.gms.maps.SupportMapFragment;
import com.UsreLaborDispatch1.www.UsreLaborDispatch1.model.LatLng;
import com.UsreLaborDispatch1.www.sync.helper.PreferenceManager;
import com.google.gson.Gson;


import java.util.ArrayList;

import static com.UsreLaborDispatch1.www.UsreLaborDispatch1.DatabaseHelper.COMPANY_ID;
import static com.UsreLaborDispatch1.www.UsreLaborDispatch1.DatabaseHelper.JOB_DATE;
import static com.UsreLaborDispatch1.www.UsreLaborDispatch1.DatabaseHelper.PRICE_QUOTE;
import static com.UsreLaborDispatch1.www.UsreLaborDispatch1.DatabaseHelper.QUANTITY;

public class GPSActivity extends AppCompatActivity {


    Fragment supportMapFragment;
    DatabaseHelper databaseHelper;
    Spinner spinner;
    TextView jobDate, tvJobDescription, jobnumber, jobpin,companyId, starttime, stoptime, jobhours, tvStartCordinates,tvStopCordinates;

    Button btnStartCoordinates,btnStopCoordinates;
    TextView uid;
    PreferenceManager preferenceManager;

    TextView priceQuote;  TextView quantity;TextView adress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        spinner = findViewById (R.id.spinner);
        preferenceManager = PreferenceManager.Companion.getInstance(this);

        databaseHelper = DatabaseHelper.getInstance(this);
        String[] adapterCols=new String[]{DatabaseHelper.JOBNUMBER};
        int[] adapterRowViews=new int[]{R.id.text};
        Cursor cursor = databaseHelper.getAllJobCursorTable2(preferenceManager.getUserPin(),preferenceManager.getCompanyId());
        SimpleCursorAdapter sca=new SimpleCursorAdapter(this, R.layout.spinner_layout, cursor, adapterCols, adapterRowViews,0);
        spinner.setAdapter(sca);


        uid = (TextView) findViewById(R.id.uid);
        jobDate = findViewById(R.id.job_date);
        tvJobDescription = findViewById(R.id.tvJobDescription);
        jobnumber = findViewById(R.id.jobnumber);
        jobpin = findViewById(R.id.jobpin);
        companyId = findViewById(R.id.companyId);
        starttime = findViewById(R.id.starttime);
        stoptime = findViewById(R.id.stoptime);
        jobhours = findViewById(R.id.jobhours);
        tvStartCordinates = findViewById(R.id.tvStartCordinates);
        tvStopCordinates = findViewById(R.id.tvStopCordinates);

        priceQuote = (TextView) findViewById(R.id.priceQuote);
        quantity = (TextView) findViewById(R.id.quantity);
        adress = (TextView) findViewById(R.id.adress);

        btnStartCoordinates = findViewById(R.id.btnStartCoordinates);
        btnStartCoordinates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cursor cursor = (Cursor) spinner.getSelectedItem();
                if(cursor.getCount() == 0) {
                    //show message
                    showMessage("Error","No Records Found");
                    Message.message (getApplicationContext (),"No Records Found");
                    return;

                }

                //cursor.moveToFirst();

                double startLatitude= cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.START_LATITUDE));
                double startLongitude = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.START_LONGITUDE));

                if(startLatitude==0&&startLongitude==00)
                {
                    Message.message(getApplicationContext(), "Job not started yet");return;
                }

                setMapMaker(new LatLng(startLatitude,startLongitude));

            }
        });
        btnStopCoordinates = findViewById(R.id.btnStopCoordinates);
        btnStopCoordinates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cursor cursor = (Cursor) spinner.getSelectedItem();
                if(cursor.getCount() == 0) {
                    //show message
                    showMessage("Error","No Records Found");
                    Message.message (getApplicationContext (),"No Records Found");
                    return;

                }


                double stopLatitude= cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.STOP_LATITUDE));
                double stopLongitude = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.STOP_LONGITUDE));

                if(stopLatitude==0&&stopLongitude==00)
                {
                    Message.message(getApplicationContext(), "Job not stopped yet");return;
                }

                setMapMaker(new LatLng(stopLatitude,stopLongitude));

            }
        });
        //
        FragmentManager fmanager = getSupportFragmentManager();
        Fragment fragment = fmanager.findFragmentById(R.id.map);
        supportMapFragment = fragment;
        //


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fillData(id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setMapMaker(LatLng latLng) {

        MapActivity.start(this,latLng);
    }


    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
    Gson gson = new Gson();
    JobLocation jobLocation = null;
    private void fillData(long id) {

        uid.setText(id+"");
        Cursor cursor = (Cursor) spinner.getSelectedItem();
        //cursor.moveToFirst();
        Double laong = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.START_LONGITUDE));
        jobDate.setText("Job Date: " + cursor.getString(cursor.getColumnIndex(JOB_DATE)));
        tvJobDescription.setText("Description: " + cursor.getString(2));
        jobnumber.setText("Job Number: " + cursor.getString(1));
        jobpin.setText("PIN: " + cursor.getString(10));
        companyId.setText("Company ID: " + cursor.getString(cursor.getColumnIndex(COMPANY_ID)));
        starttime.setText("Last Start Time: " + cursor.getString(6));
        stoptime.setText("Last Stop Time: " + cursor.getString(7));
        jobhours.setText("Hours: " + cursor.getString(3));
        tvStartCordinates.setText(String.format("%.4f", cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.START_LATITUDE)))+","+String.format("%.4f",cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.START_LONGITUDE))));
        tvStopCordinates.setText(String.format("%.4f",cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.STOP_LATITUDE)))+","+String.format("%.4f",cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.STOP_LONGITUDE))));


        priceQuote.setText("Price Quote: " + cursor.getString(cursor.getColumnIndex(PRICE_QUOTE)));
        quantity.setText("Quantity: " + cursor.getString(cursor.getColumnIndex(QUANTITY)));

//        try {
//
//            String location = cursor.getString(cursor.getColumnIndex(DatabaseHelper.JOB_ADDRESS));
//            jobLocation = gson.fromJson(location, JobLocation.class);
//            if (!jobLocation.toString().isEmpty()) {
//
//                adress.setText(jobLocation.toString());
//                adress.setVisibility(View.VISIBLE);
//                // btnShowAdress.setVisibility(View.VISIBLE);
//
//            }
//
//
//        } catch (Exception ex) {
//            jobLocation = null;
//        }

    }
}
