package com.anamika;

import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;

import adapter.NotificationAdapter;

public class NotificationActivity extends CommonAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        ListView lv_notificaion = (ListView) findViewById(R.id.lv_notificaion);

        NotificationAdapter adapter = new NotificationAdapter(this, new JSONArray());
        lv_notificaion.setAdapter(adapter);

    }
}
