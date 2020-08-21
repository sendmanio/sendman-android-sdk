package io.sendman.example.application;

import android.app.Application;
import android.content.Context;

import io.sendman.sendman.SendMan;

public class ExampleApplication extends Application {

    private static ExampleApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        SendMan.getInstance().onCreate(this);
    }

    /** --- Static Methods --- */

    public static Context getContext(){
        return instance.getApplicationContext();
    }

}
