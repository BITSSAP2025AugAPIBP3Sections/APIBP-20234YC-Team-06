package com.privychat.model;

import com.privychat.model.enums.DeliveryStatus;
import com.privychat.model.scalar.ObjectID;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Message {
    private ObjectID id;
    private ObjectID chatId;
    private ObjectID senderId;
    private String body;
    private List<Attachment> attachments = new ArrayList<>();
    private DeliveryStatus deliveryStatus;
    private ObjectID replyTo;
    private Instant serverTimestamp;

    public Message() {}

    public Message(ObjectID id, ObjectID chatId, ObjectID senderId, String body, List<Attachment> attachments, DeliveryStatus deliveryStatus, ObjectID replyTo, Instant serverTimestamp) {
        this.id = id;
        this.chatId = chatId;
        this.senderId = senderId;
        this.body = body;
        if (attachments != null) this.attachments = attachments;
        this.deliveryStatus = deliveryStatus;
        this.replyTo = replyTo;
        this.serverTimestamp = serverTimestamp;
    }

    public ObjectID getId() { return id; }
    public void setId(ObjectID id) { this.id = id; }
    public ObjectID getChatId() { return chatId; }
    public void setChatId(ObjectID chatId) { this.chatId = chatId; }
    public ObjectID getSenderId() { return senderId; }
    public void setSenderId(ObjectID senderId) { this.senderId = senderId; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public List<Attachment> getAttachments() { return attachments; }
    public void setAttachments(List<Attachment> attachments) { this.attachments = attachments != null ? attachments : new ArrayList<>(); }
    public DeliveryStatus getDeliveryStatus() { return deliveryStatus; }
    public void setDeliveryStatus(DeliveryStatus deliveryStatus) { this.deliveryStatus = deliveryStatus; }
    public ObjectID getReplyTo() { return replyTo; }
    public void setReplyTo(ObjectID replyTo) { this.replyTo = replyTo; }
    public Instant getServerTimestamp() { return serverTimestamp; }
    public void setServerTimestamp(Instant serverTimestamp) { this.serverTimestamp = serverTimestamp; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        Message message = (Message) o;
        return Objects.equals(id, message.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}

