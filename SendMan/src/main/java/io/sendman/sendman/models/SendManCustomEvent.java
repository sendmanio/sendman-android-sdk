package io.sendman.sendman.models;

public class SendManCustomEvent {
    private String key;
    private Object value;
    private long timestamp;

    public SendManCustomEvent(String key, Object value, long timestamp) {
        this.key = key;
        this.value = value;
        this.timestamp = timestamp;
    }
}
