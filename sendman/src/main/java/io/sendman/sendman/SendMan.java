package io.sendman.sendman;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.sendman.sendman.models.SendManCategory;

public class SendMan {

    private final static String SM_TOKEN = "SMToken";
    private final static String SM_TOKEN_TYPE = "SMTokenType";

    private static SendMan instance = null;

    private SendManConfig config;
    private String smUserId;
    private List<SendManCategory> categories;
    private Context applicationContext;

    public synchronized static SendMan getInstance() {
        if (instance == null) {
            instance = new SendMan();
        }
        return instance;
    }

    public static SendManConfig getConfig() {
        return SendMan.getInstance().config;
    }

    public static String getUserId() {
        return SendMan.getInstance().smUserId;
    }

    public static List<SendManCategory> getCategories() {
        return SendMan.getInstance().categories != null ? SendMan.getInstance().categories  : new ArrayList<SendManCategory>();
    }

    public static void setAppConfig(SendManConfig config) {
        SendMan.getInstance().config = config;
    }

    public static void setUserId(String smUserId) {
        SendMan.getInstance().smUserId = smUserId;
        SendManDataCollector.getInstance().startSession();
        SendManAPIHandler.getCategories(null);
    }

    public static void setFCMToken(@NonNull String token) {
        Map <String, String> properties = new HashMap<>();
        properties.put(SM_TOKEN, token);
        properties.put(SM_TOKEN_TYPE, "fcm");
        SendManDataCollector.setSdkProperties(properties);
    }

    public static void setUserCategories(List<SendManCategory> categories) {
        SendMan.getInstance().categories = categories;
    }

    public static void setUserProperties(Map<String, ?> properties) {
        SendManDataCollector.setUserProperties(properties);
    }

    public static void onCreate(final Context context) {
        SendMan.getInstance().applicationContext = context;
        SendManLifecycleHandler.getInstance().onCreate();
    }

    static Context getApplicationContext() {
        return SendMan.getInstance().applicationContext;
    }
}
