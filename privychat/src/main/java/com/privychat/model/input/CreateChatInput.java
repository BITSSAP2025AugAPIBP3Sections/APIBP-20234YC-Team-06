package com.privychat.model.input;

import com.privychat.model.enums.ChatType;
import com.privychat.model.scalar.ObjectID;
import java.util.ArrayList;
import java.util.List;

public class CreateChatInput {
    private ChatType type;
    private List<ObjectID> participantIds = new ArrayList<>();
    private String name;
    private ObjectID owner;

    public CreateChatInput() {}

    public CreateChatInput(ChatType type, List<ObjectID> participantIds, String name, ObjectID owner) {
        this.type = type;
        if (participantIds != null) this.participantIds = participantIds;
        this.name = name;
        this.owner = owner;
    }

    public ChatType getType() { return type; }
    public void setType(ChatType type) { this.type = type; }
    public List<ObjectID> getParticipantIds() { return participantIds; }
    public void setParticipantIds(List<ObjectID> participantIds) { this.participantIds = participantIds != null ? participantIds : new ArrayList<>(); }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public ObjectID getOwner() { return owner; }
    public void setOwner(ObjectID owner) { this.owner = owner; }
}

