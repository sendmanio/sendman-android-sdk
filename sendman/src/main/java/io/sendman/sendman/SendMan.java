package io.sendman.sendman;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import io.sendman.sendman.models.SendManCategory;

public class SendMan {

    private final static String SM_FCM_TOKEN = "SMFCMToken";

    private static SendMan instance = null;

    private SendManConfig config;
    private String smUserId;
    private ArrayList<SendManCategory> categories;


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

    public static ArrayList<SendManCategory> getCategories() {
        return SendMan.getInstance().categories != null ? SendMan.getInstance().categories  : new ArrayList<SendManCategory>();
    }

    public static void setAppConfig(SendManConfig config) {
        SendMan.getInstance().config = config;
    }

    public static void setUserId(String smUserId) { //TODO
        SendMan.getInstance().smUserId = smUserId;
        SendManDataCollector.getInstance().startSession();
        SendManAPIHandler.getCategories(null);
    }

    public static void setFCMToken(@NonNull String token) {
        SendManDataCollector.setSdkProperties(Collections.singletonMap(SM_FCM_TOKEN, token));
    }

    public static void setUserCategories(ArrayList<SendManCategory> categories) {
        SendMan.getInstance().categories = categories;
    }

    public static void setUserProperties(HashMap<String, String> properties) {
        SendManDataCollector.setUserProperties(properties);
    }

    public static void addUserEvent(String eventName) {
        SendMan.addGenericUserEvent(eventName, null);
    }

    public static void addUserEvent(String eventName, String value) {
        SendMan.addGenericUserEvent(eventName, value);
    }

    public static void addUserEvent(String eventName, int value) {
        SendMan.addGenericUserEvent(eventName, value);
    }

    public static void addUserEvent(String eventName, Boolean value) {
        SendMan.addGenericUserEvent(eventName, value);
    }

    public static void onCreate(final Context context) {
        SendManLifecycleHandler.getInstance().onCreate(context);
    }

    private static void addGenericUserEvent(String eventName, Object value) {
        HashMap<String, Object> events = new HashMap<>();
        events.put(eventName, value);
        SendManDataCollector.addUserEvents(events);
    }
}