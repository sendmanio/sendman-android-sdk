package io.sendman.sendman;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.sendman.sendman.models.SendManData;
import io.sendman.sendman.models.SendManPropertyValue;
import io.sendman.sendman.models.SendManSDKEvent;
import okhttp3.Response;

public class SendManDataCollector {

    private static final String TAG = SendManDataCollector.class.getSimpleName();
    private static SendManDataCollector instance = null;

    private final ScheduledExecutorService pollingExecutor;

    private Runnable pollWithDataRunnable;
    private Runnable persistRunnable;

    private final Map<String, SendManPropertyValue> customProperties;
    private final Map<String, SendManPropertyValue> sdkProperties;
    private final List<SendManSDKEvent> sdkEvents;

    private int exponentialNetworkFailureBackOff = 1;

    private boolean checkActiveUser = true;

    public synchronized static SendManDataCollector getInstance() {
        if (instance == null) {
            instance = new SendManDataCollector();
        }
        return instance;
    }

    public SendManDataCollector() {
        this.customProperties = new HashMap<>();
        this.sdkProperties = new HashMap<>();
        this.sdkEvents = new ArrayList<>();

        this.pollingExecutor = Executors.newScheduledThreadPool(1);
        this.pollForNewData(2, false);
        this.pollForNewData(60, true);
    }

    private void pollForNewData(final int secondsInterval, final Boolean persistSession) {
        Runnable runnable = new Runnable() {
            public void run() {
                SendManDataCollector.getInstance().sendData(persistSession);
                pollingExecutor.schedule(
                        persistSession ? persistRunnable : pollWithDataRunnable,
                        secondsInterval * exponentialNetworkFailureBackOff,
                        TimeUnit.SECONDS);
            }
        };
        pollingExecutor.schedule(runnable, secondsInterval, TimeUnit.SECONDS);
        saveRunnable(runnable, persistSession);
    }

    private void saveRunnable(Runnable runnable, Boolean persistSession) {
        if (persistSession) {
            persistRunnable = runnable;
        } else {
            pollWithDataRunnable = runnable;
        }
    }

    public void startSession() {
        SendManDataCollector collector = SendManDataCollector.getInstance();
        SendManSessionManager.getInstance().getOrCreateSession();
        SendManDataCollector.addPropertiesToMap(SendManDataEnricher.getUserEnrichedData(), collector.sdkProperties);
    }

    private static void addPropertiesToMap(Map<String, ?> newProperties, Map<String, SendManPropertyValue> currProperties) {
        long now = new Date().getTime();
        for (Map.Entry<String, ?> property : newProperties.entrySet()) {
            String key = property.getKey();
            Object value = property.getValue();
            if (value == null) {
                Log.e(TAG, "Discarding property \"" + key + "\" because it has a null value.");
            } else {
                if (value instanceof String) {
                    currProperties.put(key, new SendManPropertyValue((String) value, now));
                } else if (value instanceof Number) {
                    currProperties.put(key, new SendManPropertyValue((Number) value, now));
                } else if (value instanceof Boolean) {
                    currProperties.put(key, new SendManPropertyValue((Boolean) value, now));
                } else {
                    Log.e(TAG, "Discarding property \"" + property.getKey() + "\" due to unsupported type. Supported types are Number, Boolean and String. Provided type: " + property.getValue().getClass().getSimpleName());
                }
            }
        }
    }

    public static void setUserProperties(Map<String, ?> properties) {
        addPropertiesToMap(properties, SendManDataCollector.getInstance().customProperties);
    }

    public static void setSdkProperties(Map<String, ?> properties) {
        addPropertiesToMap(properties, SendManDataCollector.getInstance().sdkProperties);
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
        if (SendMan.getConfig() == null || SendMan.getUserId() == null || (!persistSession && this.customProperties.isEmpty() && this.sdkProperties.isEmpty() && this.sdkEvents.isEmpty())) {
            return;
        }
        Log.d(TAG, "Preparing to submit periodical data to API");

        final SendManData data = new SendManData();
        if (this.checkActiveUser) data.setCheckActiveUser(true);

        String userId = SendMan.getUserId();
        String autoUserId = new SendManDatabase(SendMan.getApplicationContext()).getAutoUserId();
        if (!userId.equals(autoUserId)) {
            data.setAutoUserId(autoUserId);
        }
        data.setExternalUserId(userId);

        data.setCurrentSession(SendManSessionManager.getInstance().getOrCreateSession());

        final HashMap<String, SendManPropertyValue> currentCustomProperties = new HashMap<>(this.customProperties);
        data.setCustomProperties(currentCustomProperties);
        this.customProperties.clear();

        final Map<String, SendManPropertyValue> currentSDKProperties = new HashMap<>(this.sdkProperties);
        data.setSdkProperties(currentSDKProperties);
        this.sdkProperties.clear();

        final ArrayList<SendManSDKEvent> currentSDKEvents =  new ArrayList<>(this.sdkEvents);
        data.setSdkEvents(currentSDKEvents);
        this.sdkEvents.clear();

        SendManAPIHandler.sendData(data, new SendManAPIHandler.APICallback() {
            @Override
            public void onDataSendSuccess() {
                if (data.getAutoUserId() != null) { // This means auto Id was just overridden in the backend by an actual externalUserId
                    new SendManDatabase(SendMan.getApplicationContext()).removeAutoUserId();
                }
                SendManDataCollector.this.checkActiveUser = false;
                SendManDataCollector.this.exponentialNetworkFailureBackOff = 1;
            }
            @Override
            public void onDataSendError(Response response) {

                if (SendManDataCollector.this.customProperties != null) {
                    for(Map.Entry<String, SendManPropertyValue> property : SendManDataCollector.this.customProperties.entrySet()) {
                        currentCustomProperties.put(property.getKey(), property.getValue());
                    }
                    SendManDataCollector.this.customProperties.clear();
                    SendManDataCollector.this.customProperties.putAll(currentCustomProperties);
                }

                if (SendManDataCollector.this.sdkProperties != null) {
                    for (Map.Entry<String, SendManPropertyValue> property : SendManDataCollector.this.sdkProperties.entrySet()) {
                        currentSDKProperties.put(property.getKey(), property.getValue());
                    }
                    SendManDataCollector.this.sdkProperties.clear();
                    SendManDataCollector.this.sdkProperties.putAll(currentSDKProperties);
                }

                if (SendManDataCollector.this.sdkEvents != null) {
                    currentSDKEvents.addAll(SendManDataCollector.this.sdkEvents);
                    SendManDataCollector.this.sdkEvents.clear();
                    SendManDataCollector.this.sdkEvents.addAll(currentSDKEvents);
                }

                if (response != null && response.code() == 401) {
                    if (!pollingExecutor.isShutdown() && !pollingExecutor.isTerminated()) {
                        Log.e(TAG, "Wrong App Key or Secret - will stop sending data");
                        pollingExecutor.shutdown();
                    }
                } else if (response == null) {
                    SendManDataCollector.this.exponentialNetworkFailureBackOff *= 2;
                    Log.e(TAG, "A network error has occurred while submitting periodical data to API, setting backoff multiplier to " + exponentialNetworkFailureBackOff);
                } else {
                    Log.e(TAG, "An unknown error has occurred while submitting periodical data to API");
                }
            }

        });
    }
}