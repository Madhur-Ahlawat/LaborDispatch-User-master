package com.UsreLaborDispatch1.www.UsreLaborDispatch1;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Editjob1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editjob1);

        View Editchoice1 =findViewById(R.id.editchoice1);
        Editchoice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addjobdpage = new Intent(getApplicationContext(),EditjobActivity.class);
                startActivity(addjobdpage);
            }
        });
        View Editchoice2 = findViewById(R.id.editchoice2);
        Editchoice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startjob = new Intent(getApplicationContext(),Editjob2Activity.class);
                startActivity(startjob);
            }
        });
    }
}
