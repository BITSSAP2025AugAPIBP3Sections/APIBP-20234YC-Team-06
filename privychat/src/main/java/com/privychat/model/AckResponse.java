package com.privychat.model;

public class AckResponse {
    private boolean acknowledged;

    public AckResponse() {}
    public AckResponse(boolean acknowledged) { this.acknowledged = acknowledged; }

    public boolean isAcknowledged() { return acknowledged; }
    public void setAcknowledged(boolean acknowledged) { this.acknowledged = acknowledged; }
}

