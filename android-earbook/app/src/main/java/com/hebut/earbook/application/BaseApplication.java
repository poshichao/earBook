package com.hebut.earbook.application;

import android.app.Application;

import com.beardedhen.androidbootstrap.TypefaceProvider;

public class BaseApplication extends Application {
    @Override public void onCreate() {
        super.onCreate();
        TypefaceProvider.registerDefaultIconSets();
    }
}
