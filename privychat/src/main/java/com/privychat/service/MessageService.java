package com.privychat.service;

import com.privychat.model.AckResponse;
import com.privychat.model.Attachment;
import com.privychat.model.Message;
import com.privychat.model.enums.DeliveryStatus;
import com.privychat.model.input.AttachmentInput;
import com.privychat.model.input.SendMessageInput;
import org.bson.types.ObjectId;
import com.privychat.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message sendMessage(ObjectId senderId, SendMessageInput input) {
        Message msg = new Message();
        msg.setChatId(input.getChatId());
        msg.setSenderId(senderId);
        msg.setBody(input.getBody());
        List<Attachment> attachments = input.getAttachments() == null ? List.of() :
                input.getAttachments().stream().map(this::toAttachment).collect(Collectors.toList());
        msg.setAttachments(attachments);
        msg.setReplyTo(input.getReplyTo());
        msg.setDeliveryStatus(DeliveryStatus.sent);
        msg.setServerTimestamp(Instant.now());
        return messageRepository.save(msg);
    }

    public List<Message> listMessages(ObjectId chatId, int limit) {
        return messageRepository.findByChat(chatId, limit);
    }

    public AckResponse acknowledge(ObjectId id) {
        Optional<Message> msgOpt = messageRepository.findById(id);
        msgOpt.ifPresent(m -> {
            m.setDeliveryStatus(DeliveryStatus.delivered);
            messageRepository.save(m);
        });
        return new AckResponse(true);
    }

    private Attachment toAttachment(AttachmentInput in) {
        Attachment a = new Attachment();
        a.setType(in.getType());
        a.setUrl(in.getUrl());
        return a;
    }
}
