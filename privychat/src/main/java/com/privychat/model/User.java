package com.privychat.model;

import com.privychat.model.scalar.ObjectID;
import java.time.Instant;
import java.util.Objects;

public class User {
    private ObjectID id;
    private String username;
    private String publicKey;
    private String avatarUrl;
    private Instant createdAt;

    public User() {}

    public User(ObjectID id, String username, String publicKey, String avatarUrl, Instant createdAt) {
        this.id = id;
        this.username = username;
        this.publicKey = publicKey;
        this.avatarUrl = avatarUrl;
        this.createdAt = createdAt;
    }

    public ObjectID getId() { return id; }
    public void setId(ObjectID id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPublicKey() { return publicKey; }
    public void setPublicKey(String publicKey) { this.publicKey = publicKey; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username='" + username + '\'' + '}';
    }
}

