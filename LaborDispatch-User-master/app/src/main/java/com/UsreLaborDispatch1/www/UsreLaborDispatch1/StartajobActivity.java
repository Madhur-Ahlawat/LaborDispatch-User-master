package com.UsreLaborDispatch1.www.UsreLaborDispatch1;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.UsreLaborDispatch1.www.UsreLaborDispatch1.pdf.PdfViewer;
import com.UsreLaborDispatch1.www.UsreLaborDispatch1.tools.LocationTrack;
import com.UsreLaborDispatch1.www.sync.data.JobLocation;
import com.UsreLaborDispatch1.www.sync.helper.PreferenceManager;
import com.UsreLaborDispatch1.www.sync.helper.Tools;
import com.UsreLaborDispatch1.www.sync.model.Job;
import com.UsreLaborDispatch1.www.sync.viewmodel.JobViewModel;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.pakistanitests.repo.NotificationViewModel;
import com.pakistanitests.ui.api.Notification;

import java.io.File;

import static com.UsreLaborDispatch1.www.UsreLaborDispatch1.DatabaseHelper.COMPANY_ID;
import static com.UsreLaborDispatch1.www.sync.helper.ToolsKt.dialPhoneNumber;
import static com.UsreLaborDispatch1.www.sync.helper.ToolsKt.downloadFile;
import static com.UsreLaborDispatch1.www.sync.helper.ToolsKt.getLocalPdfFile;
import static com.UsreLaborDispatch1.www.sync.helper.ToolsKt.showMsg;

public class StartajobActivity extends AppCompatActivity implements LocationTrack.LocationUpdate {

    DatabaseHelper myDb;
    TextView jobdesc;
    EditText jobhours;
    EditText jobrate;
    TextView jobflag;
    TextView starttime;
    TextView stoptime;
    TextView jobpin;
    TextView jobDate;

    TextView adress;
    Button btnShowAdress;

    TextView priceQuote;
    TextView quantity;


    TextView companyId;

    Button viewbutton;
    Button startbutton;
    Button btnDownloadPdf;
    Button btnOpenPdf;
    Button btnCall;


    String t1;
    String t2;
    String t3;
    String t5;
    String t6;
    String t7;
    String spinnervalue;
    String searchstring;
    TextView jobnumber;
    TextView tvPdfFileName;
    Spinner spinner;

    // location service
    Location location;
    LocationTrack locationTrack;
    JobViewModel jobViewModel;

    String pdfFileUrl = "";
    String pdfFileName = "";

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(onComplete);
        if (locationTrack != null)
            locationTrack.stopListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeControllsVisibility(false);
        locationTrack = new LocationTrack(this, this);
        if (!locationTrack.canGetLocation()) {
            locationTrack.showSettingsAlert();
        }

        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    View progressBar;
    View updating;

    void changeControllsVisibility(boolean enabled) {
        // viewbutton.setEnabled(enabled);
        startbutton.setEnabled(enabled);
        int visibility = enabled ? View.GONE : View.VISIBLE;
        progressBar.setVisibility(visibility);
        updating.setVisibility(visibility);

    }

    @Override
    public void onLocationUpdate(Location location) {
        if (location != null) {
            changeControllsVisibility(true);
            this.location = location;
        }
    }

    //location service

    PreferenceManager preferenceManager;
    NotificationViewModel notificationViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startajob);

        //location service
        progressBar = findViewById(R.id.progressBar);
        updating = findViewById(R.id.updating);
        //location service
        preferenceManager = PreferenceManager.Companion.getInstance(this);

        spinner = findViewById(R.id.spinner);
        myDb = DatabaseHelper.getInstance(this);


        spinner.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_layout, myDb.getAllJobcodes1(preferenceManager.getUserPin(), preferenceManager.getCompanyId())));


        jobnumber = (TextView) findViewById(R.id.jobnumber);
        jobdesc = (TextView) findViewById(R.id.jobdesc);
        jobpin = (TextView) findViewById(R.id.jobpin);
        companyId = (TextView) findViewById(R.id.companyId);

        adress = findViewById(R.id.adress);
        btnShowAdress = findViewById(R.id.btnShowAdress);
        btnCall = findViewById(R.id.btnCall);

        priceQuote = findViewById(R.id.priceQuote);
        quantity = findViewById(R.id.quantity);


        jobflag = (TextView) findViewById(R.id.jobflag);
        starttime = (TextView) findViewById(R.id.starttime);
        stoptime = (TextView) findViewById(R.id.stoptime);
        startbutton = (Button) findViewById(R.id.startbutton);
        btnDownloadPdf = findViewById(R.id.btnDownloadPdf);
        btnOpenPdf = findViewById(R.id.btnOpenPdf);
        tvPdfFileName = findViewById(R.id.tvPdfFileName);
        jobDate = findViewById(R.id.job_date);

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

        updateData();
        jobViewModel = new ViewModelProvider(this).get(JobViewModel.class);

        btnDownloadPdf.setOnClickListener(view -> {
            btnDownloadPdf.setEnabled(false);
            downloadRequestId = downloadFile(StartajobActivity.this, pdfFileName, pdfFileUrl);
        });

        btnOpenPdf.setOnClickListener(view -> {
            PdfViewer.start(StartajobActivity.this, getLocalPdfFile(StartajobActivity.this, pdfFileName).getAbsolutePath());
        });

        requestStoragePersmission();

        btnShowAdress.setOnClickListener(view -> {
            if (jobLocation != null)
                AdressSelectionAcitivty.Companion.start(StartajobActivity.this, jobLocation);

        });

        btnCall.setOnClickListener(view ->{
            if (jobLocation != null)
                dialPhoneNumber(StartajobActivity.this,jobLocation.getTellePhone());
        });

        notificationViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
    }

    long downloadRequestId = -3;
    boolean permissionGranted = false;

    void requestStoragePersmission() {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        permissionGranted = true;
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        permissionGranted = false;
                        showMsg(StartajobActivity.this, "Storage Permission Denied");
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    public void updateData() {
        startbutton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!Tools.INSTANCE.isInternetConnected(StartajobActivity.this))
                            return;


                        double longitude = 0;
                        double latitude = 0;
                        if (location != null) {

                            latitude = location.getLatitude();
                            longitude = location.getLongitude();


                            Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
                        }


                        boolean isUpdated = myDb.startajob(jobnumber.getText().toString(),
                                jobflag.getText().toString(),
                                starttime.getText().toString(), latitude,
                                longitude);
                        Cursor cursor2 = myDb.getName(spinner.getSelectedItem().toString());
                        cursor2.moveToFirst();
                        jobdesc.setText("" + cursor2.getString(2));
                        jobpin.setText("" + cursor2.getString(10));


                        jobflag.setText("" + cursor2.getString(5));
                        starttime.setText("" + cursor2.getString(6));
                        stoptime.setText("" + cursor2.getString(7));


                        if (isUpdated == true) {
                            Toast.makeText(StartajobActivity.this, "Job Started", Toast.LENGTH_SHORT).show();
                            try {
                                Job job = myDb.getJob(jobnumber.getText().toString());
                                addRecordToFirebase(job);
                                notificationViewModel.sendNotificatoin(new Notification("Job Started","Job Number: "+job.getJobNumber()
                                        +" | User Pin: "+job.getJobPin(),preferenceManager.getCompanyId()));

                            }
                            catch (Exception ex){}

                        } else
                            Toast.makeText(StartajobActivity.this, "Job Not Started", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    private void addRecordToFirebase(Job job) {
        //  Job job = myDb.getJob(jobNumber);
        if (job != null)
            jobViewModel.updateJob(job);
    }

    Gson gson = new Gson();
    JobLocation jobLocation = null;

    private void fillData() {

        Cursor cursor = myDb.getName(spinner.getSelectedItem().toString());
        cursor.moveToFirst();
        jobdesc.setText("" + cursor.getString(2));
        jobpin.setText("" + cursor.getString(10));
        companyId.setText("" + cursor.getString(cursor.getColumnIndex(COMPANY_ID)));
        jobDate.setText("" + cursor.getString(cursor.getColumnIndex("JOB_DATE")));

        jobflag.setText("" + cursor.getString(5));
        starttime.setText("" + cursor.getString(6));
        stoptime.setText("" + cursor.getString(7));

        pdfFileUrl = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PDF));
        pdfFileName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PDF_FILE_NAME));

        priceQuote.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRICE_QUOTE)));
        quantity.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.QUANTITY)));


        try {

            String location = cursor.getString(cursor.getColumnIndex(DatabaseHelper.JOB_ADDRESS));
            jobLocation = gson.fromJson(location, JobLocation.class);
            if (!jobLocation.toString().isEmpty()) {

                adress.setText(jobLocation.toString());
                adress.setVisibility(View.VISIBLE);

                btnShowAdress.setVisibility(View.VISIBLE);
                btnCall.setVisibility(View.VISIBLE);
            }
            else
            {

                btnShowAdress.setVisibility(View.GONE);
                btnCall.setVisibility(View.GONE);
            }


        } catch (Exception ex) {
            jobLocation = null;
        }


        if (pdfFileUrl.isEmpty()) {
            btnDownloadPdf.setEnabled(false);
            tvPdfFileName.setText("No Document Attached");
        } else {
            checkPdfFile();
            tvPdfFileName.setText(pdfFileName);
        }


    }

    private void checkPdfFile() {
        File localFile = getLocalPdfFile(this, pdfFileName);
        if (localFile.exists()) {
            btnOpenPdf.setEnabled(true);
            btnDownloadPdf.setEnabled(false);
        } else {
            btnOpenPdf.setEnabled(false);
            btnDownloadPdf.setEnabled(true);
        }

    }


    BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (id == downloadRequestId) {
                showMsg(StartajobActivity.this, "Download Completed");
                fillData();
            }
        }
    };
}
