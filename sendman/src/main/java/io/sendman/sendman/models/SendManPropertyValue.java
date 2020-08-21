package io.sendman.sendman.models;

public class SendManPropertyValue {
    private Object value;
    private long timestamp;

    public SendManPropertyValue(String value, long timestamp) {
        this.value = value;
        this.timestamp = timestamp;
    }

    public SendManPropertyValue(Boolean value, long timestamp) {
        this.value = value;
        this.timestamp = timestamp;
    }

    public SendManPropertyValue(Number value, long timestamp) {
        this.value = value;
        this.timestamp = timestamp;
    }

}
