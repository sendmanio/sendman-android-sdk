package io.sendman.sendman;

public class SendManConfig {
    private String appKey;
    private String appSecret;
    private String serverUrl;

    public SendManConfig(String appKey, String appSecret, String serverUrl) {
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.serverUrl = serverUrl;
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
}
