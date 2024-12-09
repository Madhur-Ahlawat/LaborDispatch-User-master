package com.UsreLaborDispatch1.www.UsreLaborDispatch1;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.UsreLaborDispatch1.www.sync.helper.PreferenceManager;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.UsreLaborDispatch1.www.UsreLaborDispatch1.DatabaseHelper.COMPANY_ID;
import static com.UsreLaborDispatch1.www.UsreLaborDispatch1.DatabaseHelper.JOB_DATE;

public class ExportActivity extends AppCompatActivity {


    DatabaseHelper dataBase;
    Spinner spinner;
    TextView tvJobDescription, tvJobHour, tvJobRate, jobpin,companyId, starttime, stoptime, jobDate;

    DatePickerDialog dialogStart;
    DatePickerDialog dialogEnd;
    Button btnStartDate, btnEndDate,btnExportAll,btnExportJob,btnEmail;
    TextView tvStartDate,tvEndDate;
    Calendar startCalendar;
    Calendar endCalendar;
    public static DateFormat dateFormatForText = new SimpleDateFormat("yyyy-MM-dd");
    public static DateFormat dateFormatForDatabase= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static String EXPORTED_FILE_NAME= "LaborExport.csv";
    PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        btnExportAll = findViewById(R.id.btnExportAll);
        btnEmail = findViewById(R.id.btnEmail);
        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareFileToEmail(EXPORTED_FILE_NAME);
            }
        });

        preferenceManager = PreferenceManager.Companion.getInstance(this);
        btnExportAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    String startDate = dateFormatForDatabase.format(startCalendar.getTimeInMillis());
                    String endDate = dateFormatForDatabase.format(endCalendar.getTimeInMillis());
                    exportData(dataBase.getTable2Name(startDate,endDate),EXPORTED_FILE_NAME);

                }catch (Exception ex){}
            }
        });
        btnExportJob = findViewById(R.id.btnExportJob);
        btnExportJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    String jobNumber = spinner.getSelectedItem().toString();
                    exportData(dataBase.getTable2Name(jobNumber),EXPORTED_FILE_NAME);

                }catch (Exception ex){}
            }
        });

        btnStartDate = findViewById(R.id.btnStartDate);
        btnStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogStart = new DatePickerDialog(ExportActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        startCalendar.set(year, monthOfYear, dayOfMonth);
                        updateDatesText();

                    }

                },startCalendar .get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH));
                dialogStart.show();
            }
        });
        btnEndDate = findViewById(R.id.btnEndDate);
        btnEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogEnd = new DatePickerDialog(ExportActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        endCalendar.set(year, monthOfYear, dayOfMonth);
                        updateDatesText();
                    }

                },endCalendar .get(Calendar.YEAR), endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DAY_OF_MONTH));
                dialogEnd.show();
            }
        });

        spinner = findViewById(R.id.spinner);
        dataBase = DatabaseHelper.getInstance(this);

        spinner.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_layout, dataBase.getAllJobcodes3(preferenceManager.getUserPin(),preferenceManager.getCompanyId())));


        jobDate = findViewById(R.id.job_date);
        tvJobDescription = findViewById(R.id.tvJobDescription);
        jobpin = findViewById(R.id.jobpin);
        companyId = findViewById(R.id.companyId);
        tvJobHour = findViewById(R.id.tvJobHour);
        tvJobRate = findViewById(R.id.tvJobRate);
        starttime = findViewById(R.id.starttime);
        stoptime = findViewById(R.id.stoptime);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fillData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.HOUR_OF_DAY, 23);
        endCalendar.set(Calendar.MINUTE, 59);
        endCalendar.set(Calendar.SECOND, 59);


        updateDatesText();


    }

    private void updateDatesText() {
        String startDate = dateFormatForText.format(startCalendar.getTimeInMillis());
        String endDate = dateFormatForText.format(endCalendar.getTimeInMillis());
        tvStartDate.setText(startDate);
        tvEndDate.setText(endDate);
    }

    private void exportData(Cursor cursor,String fileName) {
        String csv ="";
        while (cursor.moveToNext())
        {
            int max = cursor.getColumnCount();
            for(int i=0;i<max;i++)
            {
                if(i!=(max-1))
                    csv += cursor.getString(i)+",";
                else
                    csv += cursor.getString(i)+"\n";

            }
        }

        if(csv.equals(""))
            Toast.makeText(this,"No Data Found..",Toast.LENGTH_SHORT).show();
        else
            saveToFile(fileName,csv);


    }

    public void saveToFile(String fileName, String csv)
    {
        try{
            String file= fileName;//+".csv";
            FileOutputStream Writer=openFileOutput(file, Context.MODE_PRIVATE);
            Writer.write(csv.getBytes());
            Writer.flush();
            Writer.close();
            Toast.makeText(this,"Job(s) Exported",Toast.LENGTH_SHORT).show();
            // shareFileToEmail(file);
        }
        catch(Exception ex)
        {
            Log.e("Exception",ex.getMessage());
        }
    }

    private void shareFileToEmail(String file) {
        File storage = getFilesDir();
        Uri uri;
        try {

            File checkFile = new File(getFilesDir()+"/"+file);
            if(!checkFile.exists())
            {
                Toast.makeText(this,"No Expored File Found",Toast.LENGTH_SHORT).show();
                return;
            }
            uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, new File(storage, file));

        }
        catch (Exception ex){
            Toast.makeText(this,"No Expored File Found",Toast.LENGTH_SHORT).show();
            return;
        }
        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.setPackage("com.google.android.gm");
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(emailIntent);
        }catch (Exception ex){

            Toast.makeText(this, "Gmail App not found..", Toast.LENGTH_SHORT).show();
        }
    }

    private void fillData() {

        try {
            Cursor cursor = dataBase.getTable2Name(spinner.getSelectedItem().toString());
            cursor.moveToFirst();
            tvJobDescription.setText("Description: " + cursor.getString(2));
            jobDate.setText("Job Date: " + cursor.getString(cursor.getColumnIndex(JOB_DATE)));
            jobpin.setText("PIN: " + cursor.getString(10));
            companyId.setText("Company ID: " + cursor.getString(cursor.getColumnIndex(COMPANY_ID)));
            tvJobHour.setText("Hours: " + cursor.getString(3));
            tvJobRate.setText("Rate: " + cursor.getString(4));

            starttime.setText("Last Start Time: " + cursor.getString(6));
            stoptime.setText("Last Stop Time: " + cursor.getString(7));


        }catch (Exception ex){
            Log.e("",ex.getLocalizedMessage());
        }
    }
}
