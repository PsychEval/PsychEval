package com.psycheval.testapp;

import android.app.Application;
import android.util.Log;

public class MyApplication extends Application {
    public MyApplication() {
        // this method fires only once per application start.
        // getApplicationContext returns null here

        //Log.i("main", "Constructor fired");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.init();
        Utils.setTwitter();
        // this method fires once as well as constructor
        // but also application has context here

        //Log.i("main", "onCreate fired");
    }
}
