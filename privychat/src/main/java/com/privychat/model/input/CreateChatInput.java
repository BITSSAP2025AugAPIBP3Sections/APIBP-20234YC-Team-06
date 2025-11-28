package com.privychat.model.input;

import com.privychat.model.enums.ChatType;
import org.bson.types.ObjectId;
import java.util.ArrayList;
import java.util.List;

public class CreateChatInput {
    private ChatType type;
    private List<ObjectId> participantIds = new ArrayList<>();
    private String name;
    private ObjectId owner;

    public CreateChatInput() {}

    public CreateChatInput(ChatType type, List<ObjectId> participantIds, String name, ObjectId owner) {
        this.type = type;
        if (participantIds != null) this.participantIds = participantIds;
        this.name = name;
        this.owner = owner;
    }

    public ChatType getType() { return type; }
    public void setType(ChatType type) { this.type = type; }
    public List<ObjectId> getParticipantIds() { return participantIds; }
    public void setParticipantIds(List<ObjectId> participantIds) { this.participantIds = participantIds != null ? participantIds : new ArrayList<>(); }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public ObjectId getOwner() { return owner; }
    public void setOwner(ObjectId owner) { this.owner = owner; }
}
