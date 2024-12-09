package com.UsreLaborDispatch1.www.UsreLaborDispatch1;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.UsreLaborDispatch1.www.sync.helper.PreferenceManager;
import com.UsreLaborDispatch1.www.sync.helper.Tools;
import com.UsreLaborDispatch1.www.sync.viewmodel.JobViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.UsreLaborDispatch1.www.UsreLaborDispatch1.DatabaseHelper.COMPANY_ID;
import static com.UsreLaborDispatch1.www.UsreLaborDispatch1.DatabaseHelper.PRICE_QUOTE;
import static com.UsreLaborDispatch1.www.UsreLaborDispatch1.DatabaseHelper.QUANTITY;

public class EditjobActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    TextView jobdesc;
    TextView jobhours;
    TextView jobrate;
    TextView jobpin;
    TextView companyId;
    TextView priceQuote;
    TextView quantity;


    TextView uid;
    String t1;
    String t2;
    String t3;
    String t4;

    String spinnervalue;
    String searchstring;
    TextView jobnumber;
    Spinner spinner;
    TextView jobcost;
    Button savebutton;
    Button clearbutton;
    Button deletebutton;
    String t10;
    Double t0;

    JobViewModel jobViewModel;
    PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editjob);
        spinner = findViewById(R.id.spinner);
        myDb = DatabaseHelper.getInstance(this);
        savebutton = (Button) findViewById (R.id.savebutton);
        clearbutton = (Button) findViewById (R.id.clearbutton);
        deletebutton = (Button) findViewById (R.id.deletebutton);

        editData();
        deleteData();
        clearData();
        preferenceManager = PreferenceManager.Companion.getInstance(this);
        spinner.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_layout,  myDb.getAllJobcodes3(preferenceManager.getUserPin(),preferenceManager.getCompanyId())));


        jobnumber = (TextView) findViewById(R.id.jobnumber);
        jobdesc = (TextView) findViewById(R.id.jobdesc);
        jobpin = (TextView) findViewById(R.id.jobpin);
        companyId = (TextView) findViewById(R.id.companyId);

        priceQuote = (TextView) findViewById(R.id.priceQuote);
        quantity = (TextView) findViewById(R.id.quantity);


        uid = (TextView) findViewById(R.id.uid);
        jobhours = (TextView) findViewById(R.id.jobhours);
        jobrate = (TextView) findViewById(R.id.jobrate);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String spinnervalue = spinner.getSelectedItem().toString();
                jobnumber.setText(spinnervalue);
                fillData();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        jobViewModel = new ViewModelProvider(this).get(JobViewModel.class);
    }

    private void fillData() {

        Cursor cursor =  myDb.getName(spinner.getSelectedItem().toString());
        cursor.moveToFirst();
        jobdesc.setText("" + cursor.getString(2));
        jobpin.setText("" + cursor.getString(10));

        companyId.setText("" + cursor.getString(cursor.getColumnIndex(COMPANY_ID)));
        jobhours.setText("" + cursor.getString(3));
        jobrate.setText("" + cursor.getString(4));
        uid.setText("" + cursor.getString(0));

        priceQuote.setText("" + cursor.getString(cursor.getColumnIndex(PRICE_QUOTE)));
        quantity.setText("" + cursor.getString(cursor.getColumnIndex(QUANTITY)));


    }
    public void deleteData() {
        deletebutton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(!Tools.INSTANCE.isInternetConnected(EditjobActivity.this))
                            return;

                        Integer deletedRecord = myDb.deleteData(jobnumber.getText().toString());

                        String t2 =  "";
                        String t3 =  "";
                        String t4 =  "";
                        String t10 =  "";
                        String t0 =  "";

                        jobdesc.setText(t2);
                        jobhours.setText(t3);
                        jobrate.setText(t4);
                        jobpin.setText(t10);
                        uid.setText(t0);

                        priceQuote.setText("");
                        quantity.setText("");

                        if(deletedRecord > 0) {
                            processDelete(jobnumber.getText().toString());
                            Toast.makeText(EditjobActivity.this, "Record Deleted", Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(EditjobActivity.this,"Record NOT Deleted",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }



    public void editData() {
        savebutton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(!Tools.INSTANCE.isInternetConnected(EditjobActivity.this))
                            return;

                        Cursor res1 = myDb.getNameedit(jobnumber.getText().toString());
                        boolean isUpdated =  myDb.editJob(jobnumber.getText().toString(),
                                jobdesc.getText().toString(),
                                jobpin.getText().toString(),
                                jobhours.getText().toString(),
                                jobrate.getText().toString(),
                                priceQuote.getText().toString(),
                                quantity.getText().toString());


                        if(isUpdated == true) {
                            Toast.makeText(EditjobActivity.this, "Job Edited & Saved", Toast.LENGTH_SHORT).show();
                            processUpdate(jobnumber.getText().toString());
                        }
                        else
                            Toast.makeText(EditjobActivity.this,"Job Edits Not Saved",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    private void processUpdate(String jobNumber) {
        jobViewModel.updateJobMajor(myDb.getJob(jobNumber));
    }

    private void processDelete(String jobNumber) {
        jobViewModel.deleteJob(jobNumber);
    }

    public void clearData() {
        clearbutton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        String t2 =  "";
                        String t3 =  "";
                        String t4 =  "";
                        String t10 =  "";
                        String t0 =  "";
                        String tid =  "";
                        jobdesc.setText(t2);
                        jobhours.setText(t3);
                        jobrate.setText(t4);
                        jobpin.setText(t10);
                        companyId.setText(tid);
                        uid.setText(t0);
                        priceQuote.setText(t10);
                        quantity.setText(t0);

                    }
                }
        );
    }


    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

}

