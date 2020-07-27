package io.sendman.sendman.models;

import java.util.UUID;

public class SendManSession {
    private UUID sessionId;
    private long start;
    private long end;

    public SendManSession(UUID sessionId, long start, long end) {
        this.sessionId = sessionId;
        this.start = start;
        this.end = end;
    }
}
