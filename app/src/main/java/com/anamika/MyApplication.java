package com.anamika;

import android.app.Application;
import android.support.multidex.MultiDex;

/**
 * Created by shreehari on 26-11-2016.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
    }
}
