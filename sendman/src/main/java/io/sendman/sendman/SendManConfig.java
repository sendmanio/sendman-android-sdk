package io.sendman.sendman;

public class SendManConfig {
    private String appKey;
    private String appSecret;
    private String serverUrl;
    private Boolean autoGenerateUsers;

    public SendManConfig(String appKey, String appSecret) {
        this(appKey, appSecret, false);
    }

    public SendManConfig(String appKey, String appSecret, Boolean autoGenerateUsers) {
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.autoGenerateUsers = autoGenerateUsers;
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

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
