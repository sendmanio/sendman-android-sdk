package io.sendman.sendman.models;

public class SendManPropertyValue {
    private Object value;
    private long timestamp;

    public SendManPropertyValue(Object value, long timestamp) {
        this.value = value;
        this.timestamp = timestamp;
    }
}
