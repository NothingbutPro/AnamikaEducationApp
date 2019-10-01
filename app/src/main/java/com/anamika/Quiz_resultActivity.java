package com.anamika;

import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;

import adapter.Quiz_result_adapter;

public class Quiz_resultActivity extends CommonAppCompatActivity {

    private ListView lv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        String subject_id = getIntent().getStringExtra("id");
        String title = getIntent().getStringExtra("title");

        getSupportActionBar().setTitle(title + getResources().getString(R.string.quiz_result));

        lv_result = (ListView) findViewById(R.id.lv_result);

        Quiz_result_adapter adapter = new Quiz_result_adapter(this, new JSONArray(), subject_id);
        lv_result.setAdapter(adapter);

    }

}
