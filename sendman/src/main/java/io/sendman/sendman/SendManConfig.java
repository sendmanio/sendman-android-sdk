package io.sendman.sendman;

public class SendManConfig {
    private String appKey;
    private String appSecret;
    private String serverUrl;

    public SendManConfig(String appKey, String appSecret) {
        this.appKey = appKey;
        this.appSecret = appSecret;
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

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
