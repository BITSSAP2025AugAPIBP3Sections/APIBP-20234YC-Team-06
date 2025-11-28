package com.privychat.model.input;

import com.privychat.model.scalar.ObjectID;
import java.util.ArrayList;
import java.util.List;

public class SendMessageInput {
    private ObjectID chatId;
    private String body;
    private List<AttachmentInput> attachments = new ArrayList<>();
    private ObjectID replyTo;

    public SendMessageInput() {}

    public SendMessageInput(ObjectID chatId, String body, List<AttachmentInput> attachments, ObjectID replyTo) {
        this.chatId = chatId;
        this.body = body;
        if (attachments != null) this.attachments = attachments;
        this.replyTo = replyTo;
    }

    public ObjectID getChatId() { return chatId; }
    public void setChatId(ObjectID chatId) { this.chatId = chatId; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public List<AttachmentInput> getAttachments() { return attachments; }
    public void setAttachments(List<AttachmentInput> attachments) { this.attachments = attachments != null ? attachments : new ArrayList<>(); }
    public ObjectID getReplyTo() { return replyTo; }
    public void setReplyTo(ObjectID replyTo) { this.replyTo = replyTo; }
}

