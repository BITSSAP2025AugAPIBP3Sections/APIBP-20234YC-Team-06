package com.privychat.model;

import com.privychat.model.enums.DeliveryStatus;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "messages")
public class Message {
    @Id
    private ObjectId id;
    private ObjectId chatId;
    private ObjectId senderId;
    private String body;
    private List<Attachment> attachments = new ArrayList<>();
    private DeliveryStatus deliveryStatus;
    private ObjectId replyTo;
    private Instant serverTimestamp;

    public Message() {}

    public Message(ObjectId id, ObjectId chatId, ObjectId senderId, String body, List<Attachment> attachments, DeliveryStatus deliveryStatus, ObjectId replyTo, Instant serverTimestamp) {
        this.id = id;
        this.chatId = chatId;
        this.senderId = senderId;
        this.body = body;
        if (attachments != null) this.attachments = attachments;
        this.deliveryStatus = deliveryStatus;
        this.replyTo = replyTo;
        this.serverTimestamp = serverTimestamp;
    }

    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }
    public ObjectId getChatId() { return chatId; }
    public void setChatId(ObjectId chatId) { this.chatId = chatId; }
    public ObjectId getSenderId() { return senderId; }
    public void setSenderId(ObjectId senderId) { this.senderId = senderId; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public List<Attachment> getAttachments() { return attachments; }
    public void setAttachments(List<Attachment> attachments) { this.attachments = attachments != null ? attachments : new ArrayList<>(); }
    public DeliveryStatus getDeliveryStatus() { return deliveryStatus; }
    public void setDeliveryStatus(DeliveryStatus deliveryStatus) { this.deliveryStatus = deliveryStatus; }
    public ObjectId getReplyTo() { return replyTo; }
    public void setReplyTo(ObjectId replyTo) { this.replyTo = replyTo; }
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
