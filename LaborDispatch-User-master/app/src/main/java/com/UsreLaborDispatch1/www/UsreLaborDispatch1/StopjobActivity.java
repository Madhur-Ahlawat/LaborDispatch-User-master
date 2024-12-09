package com.UsreLaborDispatch1.www.UsreLaborDispatch1;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.UsreLaborDispatch1.www.UsreLaborDispatch1.tools.LocationTrack;
import com.UsreLaborDispatch1.www.sync.data.JobLocation;
import com.UsreLaborDispatch1.www.sync.helper.PreferenceManager;
import com.UsreLaborDispatch1.www.sync.helper.Tools;
import com.UsreLaborDispatch1.www.sync.model.Job;
import com.UsreLaborDispatch1.www.sync.model.JobLog;
import com.UsreLaborDispatch1.www.sync.viewmodel.JobViewModel;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
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
import static com.UsreLaborDispatch1.www.sync.helper.ToolsKt.getFileNameFromUrl;
import static com.UsreLaborDispatch1.www.sync.helper.ToolsKt.getLocalImageFilesFolder;
import static com.UsreLaborDispatch1.www.sync.helper.ToolsKt.getLocalPdfFilesFolder;
import static com.UsreLaborDispatch1.www.sync.helper.ToolsKt.showImage;

public class StopjobActivity extends AppCompatActivity implements LocationTrack.LocationUpdate {

    DatabaseHelper myDb;
    TextView jobdesc;
    TextView jobpin;
    TextView companyId;
    TextView jobDate;


    EditText jobhours;
    EditText jobrate;
    TextView jobflag;
    TextView starttime;
    TextView stoptime;

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
    Button stopbutton;
    String stopinteg;

    Button btnSelectImage;
    Button btnDisplaySelectedImage;

    TextView tvImageFileName;

    TextView adress;
    Button btnShowAdress;

    TextView priceQuote;
    TextView quantity;
    // location service
    Location location;
    LocationTrack locationTrack;
    JobViewModel jobViewModel;

    String pdfFileUrl = "";
    String pdfFileName = "";


    @Override
    protected void onPause() {
        super.onPause();
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

    }

    View progressBar;
    View updating;

    void changeControllsVisibility(boolean enabled) {
        // viewbutton.setEnabled(enabled);
        stopbutton.setEnabled(enabled);
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
        setContentView(R.layout.activity_stopjob);


        //location service
        progressBar = findViewById(R.id.progressBar);
        updating = findViewById(R.id.updating);
        //location service

        preferenceManager = PreferenceManager.Companion.getInstance(this);

        spinner = findViewById(R.id.spinner);
        myDb = DatabaseHelper.getInstance(this);

        spinner.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_layout, myDb.getAllJobcodes2(preferenceManager.getUserPin(),preferenceManager.getCompanyId())));

        jobnumber = (TextView) findViewById(R.id.jobnumber);
        jobdesc = (TextView) findViewById(R.id.jobdesc);
        jobpin = (TextView) findViewById(R.id.jobpin);
        companyId = (TextView) findViewById(R.id.companyId);
        jobDate = findViewById(R.id.job_date);

        jobflag = (TextView) findViewById(R.id.jobflag);
        starttime = (TextView) findViewById(R.id.starttime);
        stoptime = (TextView) findViewById(R.id.stoptime);
        stopbutton = (Button) findViewById(R.id.stopbutton);


        adress = findViewById(R.id.adress);
        btnShowAdress = findViewById(R.id.btnShowAdress);

        priceQuote = findViewById(R.id.priceQuote);
        quantity = findViewById(R.id.quantity);

        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnDisplaySelectedImage = findViewById(R.id.btnDisplaySelectedImage);
        tvImageFileName = findViewById(R.id.tvImageFileName);

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
        requestStoragePersmission();
        btnSelectImage.setOnClickListener(view -> {
            selectAndUploadImage();
        });


        btnDisplaySelectedImage.setOnClickListener(view -> {
            showImage(StopjobActivity.this,selectedPdfFileUrl);

        });

        btnShowAdress.setOnClickListener(view -> {
            if(jobLocation!=null)
                AdressSelectionAcitivty.Companion.start(StopjobActivity.this, jobLocation);

        });

        notificationViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);

    }




    public void updateData() {
        stopbutton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (!Tools.INSTANCE.isInternetConnected(StopjobActivity.this))
                            return;

                        double longitude = 0;
                        double latitude = 0;
                        if (location != null) {

                            latitude = location.getLatitude();
                            longitude = location.getLongitude();


                            Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
                        }


                        boolean isUpdated = myDb.stopJob(jobnumber.getText().toString(),
                                jobflag.getText().toString(),
                                stoptime.getText().toString(), latitude, longitude,
                                selectedPdfFileName ,selectedPdfFileUrl,priceQuote.getText().toString().trim(),
                                quantity.getText().toString().trim(),
                                jobDate.getText().toString().trim());
                        Cursor cursor3 = myDb.getName(spinner.getSelectedItem().toString());
                        cursor3.moveToFirst();
                        jobdesc.setText("" + cursor3.getString(2));
                        jobpin.setText("" + cursor3.getString(10));



                        jobflag.setText("" + cursor3.getString(5));
                        starttime.setText("" + cursor3.getString(6));
                        stoptime.setText("" + cursor3.getString(7));


                        if (isUpdated == true) {
                            Toast.makeText(StopjobActivity.this, "Job Stopped", Toast.LENGTH_SHORT).show();
                            addRecordToFirebase();
                        } else
                            Toast.makeText(StopjobActivity.this, "Job already Stopped", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    private void addRecordToFirebase() {
        JobLog jobLog = myDb.getJobLog();
        if (jobLog != null) {
            jobViewModel.addJobLog(jobLog);
            notificationViewModel.sendNotificatoin(new Notification("Job Stopped","Job Number: "+jobLog.getJobNumber()
                    +" | User Pin: "+jobLog.getJobPin(),jobLog.getCompanyId()));
        }

        Job job = myDb.getJob(jobLog.getJobNumber());
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
            if(!jobLocation.toString().isEmpty()) {

                adress.setText(jobLocation.toString());
                adress.setVisibility(View.VISIBLE);
                btnShowAdress.setVisibility(View.VISIBLE);

            }


        } catch (Exception ex) {
            jobLocation = null;
        }

    }


    private void selectAndUploadImage() {


        ImagePicker.Companion.with(this)
                .cameraOnly()
                //.saveDir(getLocalImageFilesFolder(this))
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                        1080,
                        1080
                )    //Final image resolution will be less than 1080 x 1080(Optional)
                .start();
//        new ChooserDialog(this)
//                .withFilter(false, true, NODES.INSTANCE.getFILE_EXT())
//                .withChosenListener((dir, dirFile) -> {
//                    savePdfToCloud(dir,dirFile);
//                })
//                .build()
//                .show();
    }

    String selectedPdfFileUrl = "";
    String selectedPdfFileName = "";

    void savePdfToCloud(String url, File file) {
        showProgressDialog();
        selectedPdfFileName = getFileNameFromUrl(url);

        jobViewModel.saveImageFile(selectedPdfFileName, file).observe(this, uri -> {
            dialog.dismiss();
            if (uri != null) {

                btnDisplaySelectedImage.setEnabled(true);
                selectedPdfFileUrl = uri.toString();
                tvImageFileName.setText(selectedPdfFileName);
            } else
                tvImageFileName.setText("Unable to Upload file");
        });
    }


    void requestStoragePersmission() {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        btnSelectImage.setEnabled(true);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Tools.INSTANCE.showMsg(StopjobActivity.this, "Storage Permission Denied");
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }


    KProgressHUD dialog;

    public void showProgressDialog() {
        dialog = KProgressHUD.create(StopjobActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Uploading File")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            Uri fileUri = data.getData();
            File file = ImagePicker.Companion.getFile(data);
            savePdfToCloud(fileUri.toString(), file);
        }
    }
}

