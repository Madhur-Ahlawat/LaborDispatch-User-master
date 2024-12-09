package com.UsreLaborDispatch1.www.UsreLaborDispatch1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class DocumentsMenuActivity extends AppCompatActivity {

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity,DocumentsMenuActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents_menu);

        View Editchoice1 =findViewById(R.id.editchoice1);
        Editchoice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadsActivity.Companion.start(DocumentsMenuActivity.this);
            }
        });
        View Editchoice2 = findViewById(R.id.editchoice2);
        Editchoice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadedImageActivity.Companion.start(DocumentsMenuActivity.this);
            }
        });
    }
}