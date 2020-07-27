package io.sendman.sendman.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class SendManData {
    private String userId;
    private String externalUserId;
    private SendManSession currentSession;
    private HashMap<String, SendManPropertyValue> customProperties;
    private HashMap<String, SendManPropertyValue> sdkProperties;
    private ArrayList<SendManCustomEvent> customEvents;
    private ArrayList<SendManSDKEvent> sdkEvents;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setExternalUserId(String externalUserId) {
        this.externalUserId = externalUserId;
    }

    public void setCurrentSession(SendManSession currentSession) {
        this.currentSession = currentSession;
    }

    public void setCustomProperties(HashMap<String, SendManPropertyValue> customProperties) {
        this.customProperties = customProperties;
    }

    public void setSdkProperties(HashMap<String, SendManPropertyValue> sdkProperties) {
        this.sdkProperties = sdkProperties;
    }

    public void setCustomEvents(ArrayList<SendManCustomEvent> customEvents) {
        this.customEvents = customEvents;
    }

    public void setSdkEvents(ArrayList<SendManSDKEvent> sdkEvents) {
        this.sdkEvents = sdkEvents;
    }
}