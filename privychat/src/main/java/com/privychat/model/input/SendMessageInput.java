package com.privychat.model.input;

import org.bson.types.ObjectId;
import java.util.ArrayList;
import java.util.List;

public class SendMessageInput {
    private ObjectId chatId;
    private String body;
    private List<AttachmentInput> attachments = new ArrayList<>();
    private ObjectId replyTo;

    public SendMessageInput() {}

    public SendMessageInput(ObjectId chatId, String body, List<AttachmentInput> attachments, ObjectId replyTo) {
        this.chatId = chatId;
        this.body = body;
        if (attachments != null) this.attachments = attachments;
        this.replyTo = replyTo;
    }

    public ObjectId getChatId() { return chatId; }
    public void setChatId(ObjectId chatId) { this.chatId = chatId; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public List<AttachmentInput> getAttachments() { return attachments; }
    public void setAttachments(List<AttachmentInput> attachments) { this.attachments = attachments != null ? attachments : new ArrayList<>(); }
    public ObjectId getReplyTo() { return replyTo; }
    public void setReplyTo(ObjectId replyTo) { this.replyTo = replyTo; }
}
