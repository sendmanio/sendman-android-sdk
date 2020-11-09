package io.sendman.sendman;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.sendman.sendman.models.SendManCategory;

public class SendMan {

    private final static String SM_TOKEN = "SMToken";
    private final static String SM_TOKEN_TYPE = "SMTokenType";

    private static SendMan instance = null;

    private SendManConfig config;
    private String smUserId;
    private List<SendManCategory> categories;
    private Context applicationContext;
    private boolean sdkInitialized = false;

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

    public static boolean isSdkInitialized() {
        return SendMan.getInstance().sdkInitialized;
    }

    public static List<SendManCategory> getCategories() {
        return SendMan.getInstance().categories != null ? SendMan.getInstance().categories  : new ArrayList<SendManCategory>();
    }

    public static void setAppConfig(SendManConfig config) {
        SendMan.getInstance().config = config;
        if (config.getAutoGenerateUsers()) {
            if (SendMan.getInstance().smUserId != null) {
                SendManDatabase storage = new SendManDatabase(SendMan.getApplicationContext());
                String autoUserId = storage.getAutoUserId();
                if (autoUserId == null) {
                    autoUserId = UUID.randomUUID().toString();
                    storage.setAutoUserId(autoUserId);
                }
                SendMan.setUserIdNoValidations(autoUserId);
            } else {
                Log.e(SendMan.class.getSimpleName(), "Ignoring autoGenerateUsers because the userId has already been explicitly set.");
            }
        }

        startSessionIfInitialized();
    }

    public static void setUserId(String smUserId) {
        SendManConfig config = SendMan.getConfig();
        if (config != null && config.getAutoGenerateUsers()) {
            Log.e(SendMan.class.getSimpleName(), "Cannot set userId on autoGenerateUsers mode");
        } else {
            SendMan.setUserIdNoValidations(smUserId);
        }
    }

    private static void setUserIdNoValidations(String smUserId) {
        SendMan.getInstance().smUserId = smUserId;
        startSessionIfInitialized();
    }

    public static void setFCMToken(@NonNull String token) {
        Map <String, String> properties = new HashMap<>();
        properties.put(SM_TOKEN, token);
        properties.put(SM_TOKEN_TYPE, "fcm");
        SendManDataCollector.setSdkProperties(properties);
    }

    private static void startSessionIfInitialized() {
        SendMan sendman = SendMan.getInstance();
        if (!sendman.sdkInitialized && sendman.config != null && sendman.smUserId != null) {
            sendman.sdkInitialized = true;
            SendManDataCollector.getInstance().startSession();
            SendManAPIHandler.getCategories(null);
        }
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
