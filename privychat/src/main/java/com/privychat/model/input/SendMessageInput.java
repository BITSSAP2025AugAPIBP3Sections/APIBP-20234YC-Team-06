package com.privychat.model.input;

import java.util.ArrayList;
import java.util.List;

public class SendMessageInput {
    private String chatId; // hex string
    private String body;
    private List<AttachmentInput> attachments = new ArrayList<>();
    private String replyTo; // hex string

    public SendMessageInput() {}

    public SendMessageInput(String chatId, String body, List<AttachmentInput> attachments, String replyTo) {
        this.chatId = chatId;
        this.body = body;
        if (attachments != null) this.attachments = attachments;
        this.replyTo = replyTo;
    }

    public String getChatId() { return chatId; }
    public void setChatId(String chatId) { this.chatId = chatId; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public List<AttachmentInput> getAttachments() { return attachments; }
    public void setAttachments(List<AttachmentInput> attachments) { this.attachments = attachments != null ? attachments : new ArrayList<>(); }
    public String getReplyTo() { return replyTo; }
    public void setReplyTo(String replyTo) { this.replyTo = replyTo; }
}
