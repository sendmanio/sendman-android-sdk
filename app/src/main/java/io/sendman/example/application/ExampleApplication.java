package io.sendman.example.application;

import android.app.Application;
import android.content.Context;

public class ExampleApplication extends Application {

    private static ExampleApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    /** --- Static Methods --- */

    public static Context getContext(){
        return instance.getApplicationContext();
    }

}
