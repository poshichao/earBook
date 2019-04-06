package com.hebut.earbook;

import android.app.Application;

import com.beardedhen.androidbootstrap.TypefaceProvider;

public class BaseApplication extends Application {
    @Override public void onCreate() {
        super.onCreate();
        TypefaceProvider.registerDefaultIconSets();
    }
}
