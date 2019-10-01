package com.anamika;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by ICS on 17/03/2018.
 */

public class TimeAcitivity extends Activity {
    TextView f_time, to_time,tname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time);

        f_time = (TextView)findViewById(R.id.f_time);
        to_time = (TextView)findViewById(R.id.to_time);
        tname = (TextView)findViewById(R.id.tname);

        Intent intent = getIntent();
        String start_time = intent.getStringExtra("start_time");
        String end_time = intent.getStringExtra("end_time");
        String teacher_name = intent.getStringExtra("teacher_name");

        f_time.setText(start_time);
        to_time.setText(end_time);
        tname.setText(teacher_name);

    }

}
