package io.sendman.sendman.models;

import java.util.ArrayList;
import java.util.Map;

public class SendManData {
    private String externalUserId;
    private String autoUserId;
    private SendManSession currentSession;
    private Map<String, SendManPropertyValue> customProperties;
    private Map<String, SendManPropertyValue> sdkProperties;
    private ArrayList<SendManSDKEvent> sdkEvents;
    private boolean checkActiveUser;

    public String getAutoUserId() {
        return autoUserId;
    }

    public void setExternalUserId(String externalUserId) {
        this.externalUserId = externalUserId;
    }

    public void setAutoUserId(String autoUserId) {
        this.autoUserId = autoUserId;
    }

    public void setCurrentSession(SendManSession currentSession) {
        this.currentSession = currentSession;
    }

    public void setCustomProperties(Map<String, SendManPropertyValue> customProperties) {
        this.customProperties = customProperties;
    }

    public void setSdkProperties(Map<String, SendManPropertyValue> sdkProperties) {
        this.sdkProperties = sdkProperties;
    }

    public void setSdkEvents(ArrayList<SendManSDKEvent> sdkEvents) {
        this.sdkEvents = sdkEvents;
    }

    public void setCheckActiveUser(boolean checkActiveUser) {
        this.checkActiveUser = checkActiveUser;
    }
}