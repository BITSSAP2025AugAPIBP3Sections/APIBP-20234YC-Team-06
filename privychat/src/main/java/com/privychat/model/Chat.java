package com.privychat.model;

import com.privychat.model.enums.ChatType;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chats")
public class Chat {
    @Id
    private ObjectId id;
    private ChatType type;
    private List<ObjectId> participants = new ArrayList<>();
    private List<ParticipantKey> participantKeys = new ArrayList<>();
    private String name;
    private ObjectId owner;
    private List<RoleEntry> roles = new ArrayList<>();
    private Instant createdAt;

    public Chat() {}

    public Chat(ObjectId id, ChatType type, List<ObjectId> participants, List<ParticipantKey> participantKeys, String name, ObjectId owner, List<RoleEntry> roles, Instant createdAt) {
        this.id = id;
        this.type = type;
        if (participants != null) this.participants = participants;
        if (participantKeys != null) this.participantKeys = participantKeys;
        this.name = name;
        this.owner = owner;
        if (roles != null) this.roles = roles;
        this.createdAt = createdAt;
    }

    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }
    public ChatType getType() { return type; }
    public void setType(ChatType type) { this.type = type; }
    public List<ObjectId> getParticipants() { return participants; }
    public void setParticipants(List<ObjectId> participants) { this.participants = participants != null ? participants : new ArrayList<>(); }
    public List<ParticipantKey> getParticipantKeys() { return participantKeys; }
    public void setParticipantKeys(List<ParticipantKey> participantKeys) { this.participantKeys = participantKeys != null ? participantKeys : new ArrayList<>(); }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public ObjectId getOwner() { return owner; }
    public void setOwner(ObjectId owner) { this.owner = owner; }
    public List<RoleEntry> getRoles() { return roles; }
    public void setRoles(List<RoleEntry> roles) { this.roles = roles != null ? roles : new ArrayList<>(); }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Chat)) return false;
        Chat chat = (Chat) o;
        return Objects.equals(id, chat.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
