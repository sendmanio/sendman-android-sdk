package io.sendman.sendman;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.sendman.sendman.models.SendManCustomEvent;
import io.sendman.sendman.models.SendManData;
import io.sendman.sendman.models.SendManPropertyValue;
import io.sendman.sendman.models.SendManSDKEvent;
import io.sendman.sendman.models.SendManSession;

public class SendManDataCollector {

    private static SendManDataCollector instance = null;

    private UUID sessionId;
    private long sessionIdStartTimestamp;

    private HashMap<String, SendManPropertyValue> customProperties;
    private HashMap<String, SendManPropertyValue> sdkProperties;
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
        collector.sessionId = UUID.randomUUID();
        collector.sessionIdStartTimestamp = new Date().getTime();
        SendManDataCollector.addPropertiesToMap(SendManDataEnricher.getUserEnrichedData(), collector.sdkProperties);
    }

    private static void addPropertiesToMap(HashMap<String, String> newProperties, HashMap<String, SendManPropertyValue> currProperties) {
        long now = new Date().getTime();
        for(Map.Entry<String, String> property : newProperties.entrySet()) {
            currProperties.put(property.getKey(), new SendManPropertyValue(property.getValue(), now));
        }
    }

    public static void setUserProperties(HashMap<String, String> properties) {
        SendManDataCollector.addPropertiesToMap(properties, SendManDataCollector.getInstance().customProperties);
    }

    public static void setSdkProperties(HashMap<String, String> properties) {
        SendManDataCollector.addPropertiesToMap(properties, SendManDataCollector.getInstance().sdkProperties);
    }

    public static void addUserEvents(HashMap<String, Object> events) {
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
//        [[UNUserNotificationCenter currentNotificationCenter] getNotificationSettingsWithCompletionHandler:^(UNNotificationSettings * _Nonnull settings) {
//        event.notificationsRegistrationState = [self getRegistrationStateFromStatus:settings.authorizationStatus];
        collector.sdkEvents.add(event);
    }

    public void addSdkEvent(String key, Object value) {
        SendManDataCollector.addSdkEvent(new SendManSDKEvent(key, value));
    }

    private void sendData(Boolean persistSession) {
        if (SendMan.getConfig() == null || this.sessionId == null || (!persistSession && this.customProperties.isEmpty() && this.sdkProperties.isEmpty() && this.customEvents.isEmpty() && this.sdkEvents.isEmpty())) {
            return;
        }
        System.out.println("Preparing to send data");

        final SendManData data = new SendManData();
        data.setExternalUserId(SendMan.getUserId());
        data.setCurrentSession(new SendManSession(this.sessionId, this.sessionIdStartTimestamp, new Date().getTime()));

        final HashMap<String, SendManPropertyValue> currentCustomProperties = this.customProperties;
        data.setCustomProperties(this.customProperties);
        this.customProperties = new HashMap<String, SendManPropertyValue>();

        final HashMap<String, SendManPropertyValue> currentSDKProperties = this.sdkProperties;
        data.setSdkProperties(this.sdkProperties);
        this.sdkProperties = new HashMap<String, SendManPropertyValue>();

        final ArrayList<SendManCustomEvent> currentCustomEvents = this.customEvents;
        data.setCustomEvents(this.customEvents);
        this.customEvents = new ArrayList<SendManCustomEvent>();

        final ArrayList<SendManSDKEvent> currentSDKEvents = this.sdkEvents;
        data.setSdkEvents(this.sdkEvents);
        this.sdkEvents = new ArrayList<SendManSDKEvent>();

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


//        + (NSString *)getRegistrationStateFromStatus:(UNAuthorizationStatus)status {
//        switch (status) {
//        case UNAuthorizationStatusAuthorized:
//        return @"On";
//        case UNAuthorizationStatusNotDetermined:
//        return @"Not requested";
//        case UNAuthorizationStatusDenied:
//        return @"Off";
//default:
//        return @"Uknknown";
//        }
//        }
//
