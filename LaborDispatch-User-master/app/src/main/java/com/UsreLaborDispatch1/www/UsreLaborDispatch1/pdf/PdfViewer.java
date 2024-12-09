package com.UsreLaborDispatch1.www.UsreLaborDispatch1.pdf;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.UsreLaborDispatch1.www.UsreLaborDispatch1.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PdfViewer extends AppCompatActivity {


    public static void start(Context context) {
        context.startActivity(new Intent(context, PdfViewer.class));
    }


    PDFView pdfView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pdfView = findViewById(R.id.pdfView);
        progressBar = findViewById(R.id.progressBar);
        Intent intent = getIntent();

        if (intent.hasExtra(LOCAL)) {
            String path = intent.getStringExtra(LOCAL);
            try {
                pdfView.fromUri(Uri.fromFile(new File(path))).load();
                setTitle(path);

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
else
        if (intent.hasExtra(PATH)) {
            String path = intent.getStringExtra(PATH);
            String name = intent.getStringExtra(NAME);
            try {

                new RetrievePdfStream().execute(path);
                setTitle(name);

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        // pdfView.fromFile(file).load();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void setTitle(String path) {
        path = path.substring(path.lastIndexOf("/") + 1);
        path = path.substring(0, path.lastIndexOf("."));
        getSupportActionBar().setTitle(path);

    }

    public static String PATH = "path";
    public static String NAME = "NAME";
    public static String LOCAL = "LOCAL";

    public static void start(Context activity, String local) {
        Intent intent = new Intent(activity, PdfViewer.class);
        intent.putExtra(LOCAL, local);
        activity.startActivity(intent);
    }

    public static void start(Context activity, String path, String name) {
        Intent intent = new Intent(activity, PdfViewer.class);
        intent.putExtra(PATH, path);
        intent.putExtra(NAME, name);
        activity.startActivity(intent);
    }

    class RetrievePdfStream extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());

                }
            } catch (IOException e) {
                return null;

            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            progressBar.setVisibility(View.GONE);
            pdfView.fromStream(inputStream).load();
        }
    }
}
