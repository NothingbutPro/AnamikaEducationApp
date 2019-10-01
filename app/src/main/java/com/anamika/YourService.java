package com.anamika;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by ICS on 24/07/2018.
 */

public class YourService extends IntentService {

    public YourService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        boolean isNetworkConnected = extras.getBoolean("isNetworkConnected");
        // your code

    }
}
