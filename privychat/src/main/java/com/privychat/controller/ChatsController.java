package com.privychat.controller;

import com.privychat.model.Chat;
import com.privychat.model.enums.Role;
import com.privychat.model.input.CreateChatInput;
import org.bson.types.ObjectId;
import com.privychat.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chats")
public class ChatsController {
    private final ChatService chatService;

    public ChatsController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    public ResponseEntity<List<Chat>> list(@RequestParam("userId") String userIdHex) {
        List<Chat> chats = chatService.listChats(new ObjectId(userIdHex));
        return ResponseEntity.ok(chats);
    }

    @PostMapping
    public ResponseEntity<Chat> create(@RequestBody CreateChatInput input) {
        Chat chat = chatService.createChat(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(chat);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chat> get(@PathVariable("id") String idHex) {
        return chatService.getChat(new ObjectId(idHex))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/{id}/participants")
    public ResponseEntity<Chat> addParticipant(@PathVariable("id") String idHex,
                                               @RequestBody Map<String, String> body) {
        String userIdHex = body.get("userId");
        return chatService.addParticipant(new ObjectId(idHex), new ObjectId(userIdHex))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/{id}/roles")
    public ResponseEntity<Chat> setRole(@PathVariable("id") String idHex,
                                        @RequestBody Map<String, String> body) {
        String userIdHex = body.get("userId");
        Role role = Role.valueOf(body.get("role"));
        return chatService.setRole(new ObjectId(idHex), new ObjectId(userIdHex), role)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
