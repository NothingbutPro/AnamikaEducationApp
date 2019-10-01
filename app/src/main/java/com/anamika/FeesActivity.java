package com.anamika;

import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;

import adapter.FeesAdapter;

public class FeesActivity extends CommonAppCompatActivity {

    private ListView lv_fees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fees);

        lv_fees = (ListView) findViewById(R.id.lv_fees);

        FeesAdapter adapter = new FeesAdapter(this, new JSONArray());
        lv_fees.setAdapter(adapter);

    }
}
