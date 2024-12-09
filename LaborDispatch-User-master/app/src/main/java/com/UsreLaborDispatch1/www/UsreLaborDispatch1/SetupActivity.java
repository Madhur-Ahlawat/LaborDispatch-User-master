package com.UsreLaborDispatch1.www.UsreLaborDispatch1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.UsreLaborDispatch1.www.sync.helper.PreferenceManager;
import com.UsreLaborDispatch1.www.sync.viewmodel.JobViewModel;

public class SetupActivity extends AppCompatActivity {
    EditText etUserPin;
    EditText etCompanyId;

    PreferenceManager preferenceManager;
    JobViewModel jobViewModel;
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        etUserPin = findViewById(R.id.userPin);
        etCompanyId = findViewById(R.id.etCompanyId);
        Button btnSave = findViewById(R.id.btnSave);
        preferenceManager = PreferenceManager.Companion.getInstance(this);
        etUserPin.setText(preferenceManager.getUserPin());
        etCompanyId.setText(preferenceManager.getCompanyId());
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });

        jobViewModel = new ViewModelProvider(this).get(JobViewModel.class);
        databaseHelper = DatabaseHelper.getInstance(this);
    }

    private void saveData() {
        String userPin = etUserPin.getText().toString().trim();
        String companyId = etCompanyId.getText().toString().trim();
        if (userPin.isEmpty()) {
            etUserPin.setError("*required");
        }
        else if (companyId.isEmpty())
        {
            etCompanyId.setError("*required");
        }
        else {

            if(!userPin.equals(preferenceManager.getCompanyId()))
            {
                jobViewModel.resetData();
                databaseHelper.pergeAllJobs();
                databaseHelper.pergeAllJobLogs();

            }

            preferenceManager.setUserPin(etUserPin.getText().toString());
            preferenceManager.setCompanyId(etCompanyId.getText().toString());
            finish();
        }
    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, SetupActivity.class));
    }
}
