package com.UsreLaborDispatch1.www.UsreLaborDispatch1;


import android.database.Cursor;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.UsreLaborDispatch1.www.sync.data.JobLocation;
import com.UsreLaborDispatch1.www.sync.helper.PreferenceManager;
import com.google.gson.Gson;

import java.text.DecimalFormat;

import static com.UsreLaborDispatch1.www.UsreLaborDispatch1.DatabaseHelper.COMPANY_ID;
import static com.UsreLaborDispatch1.www.UsreLaborDispatch1.DatabaseHelper.IMAGE_FILE_NAME;
import static com.UsreLaborDispatch1.www.UsreLaborDispatch1.DatabaseHelper.JOB_ADDRESS;
import static com.UsreLaborDispatch1.www.UsreLaborDispatch1.DatabaseHelper.JOB_DATE;
import static com.UsreLaborDispatch1.www.UsreLaborDispatch1.DatabaseHelper.PDF_FILE_NAME;
import static com.UsreLaborDispatch1.www.UsreLaborDispatch1.DatabaseHelper.PRICE_QUOTE;
import static com.UsreLaborDispatch1.www.UsreLaborDispatch1.DatabaseHelper.QUANTITY;


public class ReportjobActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    TextView jobdesc;
    TextView jobhours;
    TextView jobrate;
    TextView jobflag;

    TextView jobpin;
    TextView companyId;
    TextView starttime;
    TextView stoptime;
    TextView priceQuote;
    TextView quantity;
    TextView jobDate;

    TextView tvPdfFileName;  TextView tvImageFileName;

    TextView adress;

    String t1;
    String t2;
    String t3;
    String t5;
    String t6;
    String t7;
    String spinnervalue;
    String searchstring;
    TextView jobnumber;
    Spinner spinner;
    TextView jobcost;
    Button viewonebutton;
    Button viewallbutton;
    String t43;
    Double t34;

    PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportjob);
        spinner = findViewById(R.id.spinner);
        myDb = DatabaseHelper.getInstance(this);
        viewonebutton = (Button) findViewById (R.id.viewonebutton);
        viewallbutton = (Button) findViewById (R.id.viewallbutton);
        preferenceManager = PreferenceManager.Companion.getInstance(this);
        viewAll();
        viewJob();

        spinner.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_layout,  myDb.getAllJobcodes3(preferenceManager.getUserPin(),preferenceManager.getCompanyId())));


        jobnumber = (TextView) findViewById(R.id.jobnumber);
        jobdesc = (TextView) findViewById(R.id.jobdesc);
        jobpin = (TextView) findViewById(R.id.jobpin);
        companyId = (TextView) findViewById(R.id.companyId);
        jobflag = (TextView) findViewById(R.id.jobflag);
        starttime = (TextView) findViewById(R.id.starttime);
        stoptime = (TextView) findViewById(R.id.stoptime);
        jobhours = (TextView) findViewById(R.id.jobhours);
        jobrate = (TextView) findViewById(R.id.jobrate);
        jobcost = (TextView) findViewById(R.id.jobcost);
        tvPdfFileName = (TextView) findViewById(R.id.tvPdfFileName);
        tvImageFileName = (TextView) findViewById(R.id.tvImageFileName);
        jobDate = (TextView) findViewById(R.id.job_date);

        priceQuote = (TextView) findViewById(R.id.priceQuote);
        quantity = (TextView) findViewById(R.id.quantity);
        adress = (TextView) findViewById(R.id.adress);


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


    }

    Gson gson = new Gson();
    JobLocation jobLocation = null;
    private void fillData() {

        Cursor cursor =  myDb.getName(spinner.getSelectedItem().toString());
        cursor.moveToFirst();
        jobdesc.setText("Description: " + cursor.getString(2));
        jobDate.setText("Job Date: " + cursor.getString(cursor.getColumnIndex(JOB_DATE)));
        jobpin.setText("PIN: " + cursor.getString(10));
        companyId.setText("Company ID: " + cursor.getString(cursor.getColumnIndex(COMPANY_ID)));
        jobhours.setText("Hours: " + cursor.getString(3));
        jobrate.setText("Rate: " + cursor.getString(4));
        jobflag.setText("Status: " + cursor.getString(5));
        starttime.setText("Last Start Time: " + cursor.getString(6));
        stoptime.setText("Last Stop Time: " + cursor.getString(7));
        priceQuote.setText("Price Quote: " + cursor.getString(18));
        quantity.setText("Quantity: " + cursor.getString(19));
        String t3 =  cursor.getString(3);
        String t4 =  cursor.getString(4);
        Double t34 = cursor.getDouble (3)* cursor.getDouble (4);
        t34 =Double.parseDouble(new DecimalFormat("######.####").format(t34));
        String t43 =  "Cost: "+Double.toString(t34);
        jobcost.setText(t43);

        tvPdfFileName.setText("PDF File: " + cursor.getString(16));


        //tvPdfFileName.setText(cursor.getString(cursor.getColumnIndex(PDF_FILE_NAME)));


        // priceQuote.setText(cursor.getString(cursor.getColumnIndex(PRICE_QUOTE)));
        // quantity.setText(cursor.getString(cursor.getColumnIndex(QUANTITY)));

        try {

            String location = cursor.getString(cursor.getColumnIndex(DatabaseHelper.JOB_ADDRESS));
            jobLocation = gson.fromJson(location, JobLocation.class);
            if (!jobLocation.toString().isEmpty()) {

                adress.setText(jobLocation.toString());
                adress.setVisibility(View.VISIBLE);
                // btnShowAdress.setVisibility(View.VISIBLE);

            }


        } catch (Exception ex) {
            jobLocation = null;
        }
    }

    public void viewAll() {
        viewallbutton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor res = myDb.getAllReportData(preferenceManager.getUserPin(),preferenceManager.getCompanyId());
                        if(res.getCount() == 0) {
                            //show message
                            showMessage("Error","No Records Found");
                            return;
                        }
                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()) {
                            buffer.append("UID: "+ res.getString(0)+"\n");
                            buffer.append("JOB NUMBER: "+ res.getString(1)+"\n");
                            buffer.append("JOB DATE: "+ res.getString(res.getColumnIndex(JOB_DATE))+"\n");
                            buffer.append("JOB DESC: "+ res.getString(2)+"\n");
                            buffer.append("PIN: "+ res.getString(10)+"\n");
                            buffer.append("COMPANY ID: "+ res.getString(res.getColumnIndex(COMPANY_ID))+"\n");
                            buffer.append("JOB FLAG: "+ res.getString(5)+"\n");
                            buffer.append("JOB HOURS: "+ res.getString(3)+"\n");
                            buffer.append("JOB RATE: "+ res.getString(4)+"\n");
                            Double t34 = res.getDouble (3)* res.getDouble (4);
                            t34 =Double.parseDouble(new DecimalFormat("######.####").format(t34));
                            String t43 =  Double.toString(t34);
                            buffer.append("JOBCOST : "+ t43 +"\n");
                            buffer.append("STARTTIME : "+ res.getString(6)+"\n");
                            buffer.append("STOPTIME : "+ res.getString(7)+"\n");
                            buffer.append("PDF FILE :"+ res.getString(res.getColumnIndex(PDF_FILE_NAME))+"\n");
                            buffer.append("IMAGE FILE :"+ res.getString(res.getColumnIndex(IMAGE_FILE_NAME))+"\n");


                            buffer.append("Price Quote : "+ res.getString(res.getColumnIndex(PRICE_QUOTE))+"\n");
                            buffer.append("Quantity :"+ res.getString(res.getColumnIndex(QUANTITY))+"\n\n");
                            // buffer.append("JOB ADDRESS :"+ res.getString(res.getColumnIndex(JOB_ADDRESS))+"\n\n");
                        }
                        //show all the data
                        showMessage("Data",buffer.toString());
                    }
                }
        );
    }


    public void viewJob() {
        viewonebutton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor res = myDb.getOneReportData(jobnumber.getText().toString());

                        if(res.getCount() == 0) {
                            //show message
                            showMessage("Error","No Records Found");
                            return;
                        }
                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()) {
                            buffer.append("UID: "+ res.getString(0)+"\n");
                            buffer.append("JOB NUMBER: "+ res.getString(1)+"\n");
                            buffer.append("JOB DESC: "+ res.getString(2)+"\n");
                            buffer.append("PIN: "+ res.getString(10)+"\n");
                            buffer.append("COMPANY ID: "+ res.getString(res.getColumnIndex(COMPANY_ID))+"\n");
                            buffer.append("JOB FLAG: "+ res.getString(5)+"\n");
                            buffer.append("JOB HOURS: "+ res.getString(3)+"\n");
                            buffer.append("JOB RATE: "+ res.getString(4)+"\n");
                            Double t34 = res.getDouble (3)* res.getDouble (4);
                            t34 =Double.parseDouble(new DecimalFormat("######.####").format(t34));
                            String t43 =  Double.toString(t34);
                            buffer.append("JOBCOST :"+ t43 +"\n");
                            buffer.append("STARTTIME :"+ res.getString(6)+"\n");
                            buffer.append("STOPTIME :"+ res.getString(7)+"\n");
                            buffer.append("PDF FILE :"+ res.getString(res.getColumnIndex(PDF_FILE_NAME))+"\n");
                            buffer.append("IMAGE FILE :"+ res.getString(res.getColumnIndex(IMAGE_FILE_NAME))+"\n");


                            buffer.append("Price Quote : "+ res.getString(res.getColumnIndex(PRICE_QUOTE))+"\n");
                            buffer.append("Quantity :"+ res.getString(res.getColumnIndex(QUANTITY))+"\n\n");
                        }
                        //show all the data
                        showMessage("Data",buffer.toString());
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

