package com.UsreLaborDispatch1.www.UsreLaborDispatch1;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.UsreLaborDispatch1.www.UsreLaborDispatch1.tools.LocationTrack;
import com.UsreLaborDispatch1.www.sync.helper.PreferenceManager;
import com.UsreLaborDispatch1.www.sync.model.Job;
import com.UsreLaborDispatch1.www.sync.model.JobLog;
import com.UsreLaborDispatch1.www.sync.viewmodel.JobViewModel;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {


    LocationTrack locationTrack;

    @Override
    protected void onPause() {
        super.onPause();
        if (locationTrack != null)
            locationTrack.stopListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationTrack = new LocationTrack(this, null);
        if (!locationTrack.canGetLocation()) {
            locationTrack.showSettingsAlert();
        }
//        {
//            locationTrack.stopListener();
//            Intent intent = new Intent(this, LocationTrack.class);
//            startService(intent);
//
//        }

        jobViewModel.getJobs().observe(this, new Observer<List<Job>>() {
            @Override
            public void onChanged(List<Job> jobs) {
                processJobs(jobs);
            }
        });


        jobViewModel.getJobLogs().observe(this, new Observer<List<JobLog>>() {
            @Override
            public void onChanged(List<JobLog> jobLogs) {

                processJobLogs(jobLogs);
            }
        });

        checkTrail();

    }

    private static final int MAX_DAYS = 150;

    private void checkTrail() {

        int days = (int) (System.currentTimeMillis() - preferenceManager.getTrailStartTime()) / (1000 * 60 * 60 * 24);
        if (days > MAX_DAYS) {
            Toast.makeText(this, "Trial Expired", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    private final static int ALL_PERMISSIONS_RESULT = 101;

    JobViewModel jobViewModel;

    String spinnervalue;
    DatabaseHelper myDb;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb = DatabaseHelper.getInstance(this);
        /// location system
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.

        preferenceManager = PreferenceManager.Companion.getInstance(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
        /// location system


//        View addjob =findViewById(R.id.addjob);
//
//        addjob.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent addjobdpage = new Intent(getApplicationContext(),AddjobActivity.class);
//                startActivity(addjobdpage);
//            }
//        });

        View setup = findViewById(R.id.setup);
        setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetupActivity.start(MainActivity.this);
            }
        });

        View startjob = findViewById(R.id.startbutton);
        startjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSetupReady())
                    return;
                Intent startjob = new Intent(getApplicationContext(), StartajobActivity.class);
                startActivity(startjob);
            }
        });
        View stopjob = findViewById(R.id.stopjob);
        stopjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSetupReady())
                    return;
                Intent stopjob = new Intent(getApplicationContext(), StopjobActivity.class);
                startActivity(stopjob);
            }
        });
        View reportjob = findViewById(R.id.reportjob);
        reportjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSetupReady())
                    return;
                Intent reportjob = new Intent(getApplicationContext(), ReportjobActivity.class);
                startActivity(reportjob);
            }
        });

        View editjob = findViewById(R.id.editjob);
        editjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSetupReady())
                    return;
//                Intent editjob = new Intent(getApplicationContext(), Editjob1Activity.class);
                Intent editjob = new Intent(getApplicationContext(), Editjob2Activity.class);
                startActivity(editjob);
            }
        });

        View exportjob = findViewById(R.id.exportjob);
        exportjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSetupReady())
                    return;
                Intent exportjob = new Intent(getApplicationContext(), ExportActivity.class);
                startActivity(exportjob);
            }
        });
        View gps = findViewById(R.id.gps);
        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSetupReady())
                    return;
                Intent gps = new Intent(getApplicationContext(), GPSActivity.class);
                startActivity(gps);
            }
        });

        View cardDownloads = findViewById(R.id.cardDownloads);
        cardDownloads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentsMenuActivity.start(MainActivity.this);
            }
        });


        jobViewModel = new ViewModelProvider(this).get(JobViewModel.class);

    }

    private void processJobLogs(List<JobLog> jobLogs) {
        if (jobLogs != null) {

            myDb.pergeAllJobLogs();
            for (JobLog jobLog : jobLogs) {
                myDb.addJobLog(jobLog);
            }
        }
    }

    private void processJobs(List<Job> jobs) {
        if (jobs != null) {
            myDb.pergeAllJobs();
            for (Job job : jobs) {
                myDb.addJob(job);
            }
        }
    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    Boolean isSetupReady() {

        String userPin = preferenceManager.getUserPin();
        String companyId = preferenceManager.getCompanyId();
        if (userPin.equals("") || companyId.equals("")) {
            SetupActivity.start(this);
            return false;
        } else
            return true;

    }
}
