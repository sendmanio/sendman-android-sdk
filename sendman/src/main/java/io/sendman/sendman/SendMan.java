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

    public static void setUserId(String smUserId) { //TODO
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

    public static void setUserProperties(Map<String, String> properties) {
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
