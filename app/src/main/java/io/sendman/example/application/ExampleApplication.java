package io.sendman.example.application;

import android.app.Application;

import io.sendman.sendman.SendMan;

public class ExampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SendMan.onCreate(this);
    }

}
