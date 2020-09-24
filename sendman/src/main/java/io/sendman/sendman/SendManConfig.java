package io.sendman.sendman;

import androidx.annotation.NonNull;

public class SendManConfig {
    private String appKey;
    private String appSecret;
    private String serverUrl;
    private String smallIconFilename;
    private Boolean autoGenerateUsers;

    public SendManConfig(@NonNull String appKey, @NonNull String appSecret) {
        this(appKey, appSecret, false);
    }

    public SendManConfig(@NonNull String appKey, @NonNull String appSecret, Boolean autoGenerateUsers) {
        this(appKey, appSecret, autoGenerateUsers, null);
    }

    public SendManConfig(@NonNull String appKey, @NonNull String appSecret, String smallIconFilename) {
        this(appKey, appSecret, false, smallIconFilename);
    }

    public SendManConfig(@NonNull String appKey, @NonNull String appSecret, Boolean autoGenerateUsers, String smallIconFilename) {
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.autoGenerateUsers = autoGenerateUsers;
        this.smallIconFilename = smallIconFilename;
    }

    String getAppKey() {
        return appKey;
    }

    String getAppSecret() {
        return appSecret;
    }

    String getServerUrl() {
        return serverUrl;
    }

    public Boolean getAutoGenerateUsers() {
        return autoGenerateUsers;
    }

    public String getSmallIconFilename() {
        return smallIconFilename;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
