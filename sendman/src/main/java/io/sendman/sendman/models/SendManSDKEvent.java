package io.sendman.sendman.models;

import java.util.UUID;

public class SendManSDKEvent implements SendManIdentifiable {

    private String key;
    private Object value;
    private String id;
    private String templateId;
    private String activityId;
    private long timestamp;
    private String notificationsRegistrationState;
    private String appState;

    public SendManSDKEvent(String key, Object value) {
        this.key = key;
        this.value = value;
        this.id = UUID.randomUUID().toString();
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

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    @Override
    public String getId() {
        return null;
    }
}
