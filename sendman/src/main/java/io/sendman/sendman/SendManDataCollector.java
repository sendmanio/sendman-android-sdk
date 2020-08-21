package io.sendman.sendman;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.sendman.sendman.models.SendManCustomEvent;
import io.sendman.sendman.models.SendManData;
import io.sendman.sendman.models.SendManPropertyValue;
import io.sendman.sendman.models.SendManSDKEvent;

public class SendManDataCollector {

    private static final String TAG = SendManDataCollector.class.getSimpleName();
    private static SendManDataCollector instance = null;

    private Map<String, SendManPropertyValue> customProperties;
    private Map<String, SendManPropertyValue> sdkProperties;
    private ArrayList<SendManCustomEvent> customEvents;
    private ArrayList<SendManSDKEvent> sdkEvents;

    public synchronized static SendManDataCollector getInstance() {
        if (instance == null) {
            instance = new SendManDataCollector();
        }
        return instance;
    }

    public SendManDataCollector() {
        this.customProperties = new HashMap<String, SendManPropertyValue>();
        this.sdkProperties = new HashMap<String, SendManPropertyValue>();
        this.customEvents = new ArrayList<SendManCustomEvent>();
        this.sdkEvents = new ArrayList<SendManSDKEvent>();
        this.pollForNewData(2, false);
        this.pollForNewData(60, true);
    }

    private void pollForNewData(int secondsInterval, final Boolean persistSession) {
        Runnable newDataRunnable = new Runnable() {
            public void run() {
                SendManDataCollector.getInstance().sendData(persistSession);
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(newDataRunnable, 0, secondsInterval, TimeUnit.SECONDS);
    }

    public void startSession() {
        SendManDataCollector collector = SendManDataCollector.getInstance();
        SendManSessionManager.getInstance().getOrCreateSession();
        SendManDataCollector.addPropertiesToMap(SendManDataEnricher.getUserEnrichedData(), collector.sdkProperties);
    }

    private static void addPropertiesToMap(Map<String, ?> newProperties, Map<String, SendManPropertyValue> currProperties) {
        long now = new Date().getTime();
        for (Map.Entry<String, ?> property : newProperties.entrySet()) {
            if (property.getValue() instanceof String) {
                currProperties.put(property.getKey(), new SendManPropertyValue((String) property.getValue(), now));
            } else if (property.getValue() instanceof Number) {
                currProperties.put(property.getKey(), new SendManPropertyValue((Number) property.getValue(), now));
            } else if (property.getValue() instanceof Boolean) {
                currProperties.put(property.getKey(), new SendManPropertyValue((Boolean) property.getValue(), now));
            } else {
                String message = "Discarding property \"" + property.getKey() + "\" due to unsupported type. Supported types are Number, Boolean and String. Provided type: " + property.getValue().getClass().getSimpleName();
                Log.e(TAG, message);
            }
        }
    }

    public static void setUserProperties(Map<String, ?> properties) {
        addPropertiesToMap(properties, SendManDataCollector.getInstance().customProperties);
    }

    public static void setSdkProperties(Map<String, ?> properties) {
        addPropertiesToMap(properties, SendManDataCollector.getInstance().sdkProperties);
    }

    public static void addUserEvents(Map<String, Object> events) {
        SendManDataCollector collector = SendManDataCollector.getInstance();
        long now = new Date().getTime();
        for(Map.Entry<String, Object> event : events.entrySet()) {
            collector.customEvents.add(new SendManCustomEvent(event.getKey(), event.getValue(), now));
        }
    }

    public static void addSdkEvent(SendManSDKEvent event) {
        SendManDataCollector collector = SendManDataCollector.getInstance();
        long now = new Date().getTime();
        event.setTimestamp(now);
        event.setNotificationsRegistrationState(SendManLifecycleHandler.getInstance().getNotificationRegistrationState());
        collector.sdkEvents.add(event);
    }

    public void addSdkEvent(String key, Object value) {
        SendManDataCollector.addSdkEvent(new SendManSDKEvent(key, value));
    }

    private void sendData(Boolean persistSession) {
        if (persistSession && !SendManLifecycleHandler.getInstance().isInForeground()) {
            return;
        }
        if (SendMan.getConfig() == null || SendMan.getUserId() == null || (!persistSession && this.customProperties.isEmpty() && this.sdkProperties.isEmpty() && this.customEvents.isEmpty() && this.sdkEvents.isEmpty())) {
            return;
        }
        System.out.println("Preparing to send data");

        final SendManData data = new SendManData();
        data.setExternalUserId(SendMan.getUserId());
        data.setCurrentSession(SendManSessionManager.getInstance().getOrCreateSession());

        final Map<String, SendManPropertyValue> currentCustomProperties = this.customProperties;
        data.setCustomProperties(this.customProperties);
        this.customProperties = new HashMap<>();

        final Map<String, SendManPropertyValue> currentSDKProperties = this.sdkProperties;
        data.setSdkProperties(this.sdkProperties);
        this.sdkProperties = new HashMap<>();

        final ArrayList<SendManCustomEvent> currentCustomEvents = this.customEvents;
        data.setCustomEvents(this.customEvents);
        this.customEvents = new ArrayList<>();

        final ArrayList<SendManSDKEvent> currentSDKEvents = this.sdkEvents;
        data.setSdkEvents(this.sdkEvents);
        this.sdkEvents = new ArrayList<>();

        SendManAPIHandler.sendData(data, new SendManAPIHandler.APICallback() {
            @Override
            public void onDataSendError() {
                if (SendManDataCollector.this.customProperties != null) {
                    for(Map.Entry<String, SendManPropertyValue> property : SendManDataCollector.this.customProperties.entrySet()) {
                        currentCustomProperties.put(property.getKey(), property.getValue());
                    }
                    SendManDataCollector.this.customProperties = currentCustomProperties;
                }

                if (SendManDataCollector.this.sdkProperties != null) {
                    for (Map.Entry<String, SendManPropertyValue> property : SendManDataCollector.this.sdkProperties.entrySet()) {
                        currentSDKProperties.put(property.getKey(), property.getValue());
                    }
                    SendManDataCollector.this.sdkProperties = currentSDKProperties;
                }

                if (SendManDataCollector.this.customEvents != null) {
                    currentCustomEvents.addAll(SendManDataCollector.this.customEvents);
                    SendManDataCollector.this.customEvents = currentCustomEvents;
                }

                if (SendManDataCollector.this.sdkEvents != null) {
                    currentSDKEvents.addAll(SendManDataCollector.this.sdkEvents);
                    SendManDataCollector.this.sdkEvents = currentSDKEvents;
                }

                System.out.println("failed to set properties");
            }

        });
    }
}