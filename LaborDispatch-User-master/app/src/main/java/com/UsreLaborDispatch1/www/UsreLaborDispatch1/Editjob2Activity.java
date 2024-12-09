package com.UsreLaborDispatch1.www.UsreLaborDispatch1;


import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.lifecycle.ViewModelProvider;

import com.UsreLaborDispatch1.www.sync.helper.PreferenceManager;
import com.UsreLaborDispatch1.www.sync.helper.Tools;
import com.UsreLaborDispatch1.www.sync.viewmodel.JobViewModel;

import static com.UsreLaborDispatch1.www.UsreLaborDispatch1.DatabaseHelper.COMPANY_ID;
import static com.UsreLaborDispatch1.www.UsreLaborDispatch1.DatabaseHelper.PRICE_QUOTE;
import static com.UsreLaborDispatch1.www.UsreLaborDispatch1.DatabaseHelper.QUANTITY;


public class Editjob2Activity extends AppCompatActivity {

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
        savebutton = (Button) findViewById(R.id.savebutton);
        clearbutton = (Button) findViewById(R.id.clearbutton);
        deletebutton = (Button) findViewById(R.id.deletebutton);
        preferenceManager = PreferenceManager.Companion.getInstance(this);
        editData();
        deleteData();
        clearData();

//        spinner.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_layout,  myDb.getAllJobcodes1ForTable2()));

        String[] adapterCols = new String[]{DatabaseHelper.JOBNUMBER};
        int[] adapterRowViews = new int[]{R.id.text};
        Cursor cursor = myDb.getAllJobCursorTransactopn(preferenceManager.getUserPin(), preferenceManager.getCompanyId());
        SimpleCursorAdapter sca = new SimpleCursorAdapter(this, R.layout.spinner_layout, cursor, adapterCols, adapterRowViews, 0);
        spinner.setAdapter(sca);


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
                Cursor c = (Cursor) spinner.getSelectedItem();
                String spinnervalue = c.getString(c.getColumnIndex(DatabaseHelper.JOBNUMBER));
                jobnumber.setText(spinnervalue);
                uid.setText(id + "");
                fillData();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        jobViewModel = new ViewModelProvider(this).get(JobViewModel.class);
    }


    private void fillData() {

        Cursor cursor = myDb.getTable2Name2(spinner.getSelectedItemId());
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

                        if (!Tools.INSTANCE.isInternetConnected(Editjob2Activity.this))
                            return;

                        Integer deletedRecord = myDb.deleteData(spinner.getSelectedItemId());

                        String t2 = "";
                        String t3 = "";
                        String t4 = "";
                        String t10 = "";
                        String t0 = "";

                        jobdesc.setText(t2);
                        jobhours.setText(t3);
                        jobrate.setText(t4);
                        jobpin.setText(t10);
                        uid.setText(t0);

                        priceQuote.setText("");
                        quantity.setText("");

                        if (deletedRecord > 0) {
                            processDelete("" + spinner.getSelectedItemId());
                            Toast.makeText(Editjob2Activity.this, "Record Deleted", Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(Editjob2Activity.this, "Record NOT Deleted", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    private void processDelete(String uiid) {
        jobViewModel.deleteJobLog(uiid);
    }

    public void editData() {
        savebutton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!Tools.INSTANCE.isInternetConnected(Editjob2Activity.this))
                            return;

                        // Cursor res1 = myDb.getNameedit(jobnumber.getText().toString());
                        boolean isUpdated = myDb.editJob2(spinner.getSelectedItemId(),

                                jobdesc.getText().toString(),
                                jobpin.getText().toString(),
                                jobhours.getText().toString(),
                                jobrate.getText().toString(),
                                priceQuote.getText().toString(),
                                quantity.getText().toString());


                        if (isUpdated == true) {
                            Toast.makeText(Editjob2Activity.this, "Job Edited & Saved", Toast.LENGTH_SHORT).show();
                            processUpdate("" + spinner.getSelectedItemId());
                        } else
                            Toast.makeText(Editjob2Activity.this, "Job Edits Not Saved", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    private void processUpdate(String uiid) {
        jobViewModel.updateJobLog(uiid, myDb.getJobLog(uiid));
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

