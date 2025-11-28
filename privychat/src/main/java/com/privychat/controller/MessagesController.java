package com.privychat.controller;

import com.privychat.model.AckResponse;
import com.privychat.model.Message;
import com.privychat.model.input.SendMessageInput;
import org.bson.types.ObjectId;
import com.privychat.service.MessageService;
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

    @GetMapping
    public ResponseEntity<List<Message>> list(@RequestParam("chatId") String chatIdHex,
                                              @RequestParam(value = "limit", defaultValue = "50") int limit) {
        List<Message> items = messageService.listMessages(new ObjectId(chatIdHex), limit);
        return ResponseEntity.ok(items);
    }

    @PostMapping
    public ResponseEntity<Message> send(@RequestHeader("X-User-Id") String senderIdHex,
                                        @RequestBody SendMessageInput input) {
        Message created = messageService.sendMessage(new ObjectId(senderIdHex), input);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/{id}/ack")
    public ResponseEntity<AckResponse> acknowledge(@PathVariable("id") String idHex) {
        AckResponse ack = messageService.acknowledge(new ObjectId(idHex));
        return ResponseEntity.ok(ack);
    }
}
