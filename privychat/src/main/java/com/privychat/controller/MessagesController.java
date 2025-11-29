package com.privychat.controller;

import com.privychat.model.AckResponse;
import com.privychat.model.Message;
import com.privychat.model.input.SendMessageInput;
import com.privychat.service.MessageService;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessagesController {
    private final MessageService messageService;

    public MessagesController(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * Lists messages for a specific chat with pagination.
     * GET /messages?chatId={chatId}&limit={limit}
     *
     * @param chatIdHex the chat ID in hexadecimal format (24-character ObjectId)
     * @param limit maximum number of messages to return (default: 50, min: 1, max: 200)
     * @return 200 OK with list of messages sorted by server timestamp
     */
    @GetMapping
    public ResponseEntity<List<Message>> list(@RequestParam("chatId") String chatIdHex,
                                               @RequestParam(value = "limit", defaultValue = "50") int limit) {
        ObjectId chatId = new ObjectId(chatIdHex);
        List<Message> messages = messageService.listMessages(chatId, limit);
        return ResponseEntity.ok(messages);
    }

    /**
     * Sends a new message to a chat.
     * POST /messages
     *
     * @param senderIdHex the sender user ID from X-User-Id header
     * @param input the message input data containing chatId, body, attachments, and replyTo
     * @return 201 Created with the created message object
     */
    @PostMapping
    public ResponseEntity<Message> send(@RequestHeader("X-User-Id") String senderIdHex,
                                         @RequestBody SendMessageInput input) {
        ObjectId senderId = new ObjectId(senderIdHex);
        Message message = messageService.sendMessage(senderId, input);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    /**
     * Acknowledges message delivery.
     * POST /messages/{id}/ack
     *
     * @param idHex the message ID in hexadecimal format (24-character ObjectId)
     * @return 200 OK with acknowledgement response
     */
    @PostMapping("/{id}/ack")
    public ResponseEntity<AckResponse> acknowledge(@PathVariable("id") String idHex) {
        ObjectId messageId = new ObjectId(idHex);
        AckResponse ack = messageService.acknowledge(messageId);
        return ResponseEntity.ok(ack);
    }
}
