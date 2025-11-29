package com.privychat.model.input;

import com.privychat.model.enums.ChatType;
import java.util.ArrayList;
import java.util.List;

public class CreateChatInput {
    private ChatType type;
    private List<String> participantIds = new ArrayList<>(); // hex strings
    private String name;
    private String owner; // hex string

    public CreateChatInput() {}

    public CreateChatInput(ChatType type, List<String> participantIds, String name, String owner) {
        this.type = type;
        if (participantIds != null) this.participantIds = participantIds;
        this.name = name;
        this.owner = owner;
    }

    public ChatType getType() { return type; }
    public void setType(ChatType type) { this.type = type; }
    public List<String> getParticipantIds() { return participantIds; }
    public void setParticipantIds(List<String> participantIds) { this.participantIds = participantIds != null ? participantIds : new ArrayList<>(); }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
}
