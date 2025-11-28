package com.privychat.model;

import com.privychat.model.scalar.ObjectID;
import java.util.Objects;

public class ParticipantKey {
    private ObjectID userId;
    private String encKey;

    public ParticipantKey() {}

    public ParticipantKey(ObjectID userId, String encKey) {
        this.userId = userId;
        this.encKey = encKey;
    }

    public ObjectID getUserId() { return userId; }
    public void setUserId(ObjectID userId) { this.userId = userId; }
    public String getEncKey() { return encKey; }
    public void setEncKey(String encKey) { this.encKey = encKey; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParticipantKey)) return false;
        ParticipantKey that = (ParticipantKey) o;
        return Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() { return Objects.hash(userId); }
}

