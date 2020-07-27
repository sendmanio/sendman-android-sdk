package io.sendman.sendman.models;

public class SendManSDKEvent {

    private String key;
    private Object value;
    private String messageId;
    private String activityId;
    private long timestamp;
    private String notificationsRegistrationState;
    private String appState;

    public SendManSDKEvent(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setNotificationsRegistrationState(String notificationsRegistrationState) {
        this.notificationsRegistrationState = notificationsRegistrationState;
    }

    public void setAppState(String appState) {
        this.appState = appState;
    }
}
