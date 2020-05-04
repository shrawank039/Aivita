package com.matrixdeveloper.aivita.SimpleClasses;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.FirebaseApp;

import io.fabric.sdk.android.Fabric;

public class Aivita extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        Fabric.with(this, new Crashlytics());
    }

}
