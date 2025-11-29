package com.privychat.controller;

import com.privychat.model.Chat;
import com.privychat.model.enums.Role;
import com.privychat.model.input.CreateChatInput;
import com.privychat.service.ChatService;
import org.bson.types.ObjectId;
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

    /**
     * Lists all chats for a specific user.
     * GET /chats?userId={userId}
     *
     * @param userIdHex the user ID in hexadecimal format (24-character ObjectId)
     * @return 200 OK with list of chats where the user is a participant
     */
    @GetMapping
    public ResponseEntity<List<Chat>> list(@RequestParam("userId") String userIdHex) {
        ObjectId userId = new ObjectId(userIdHex);
        List<Chat> chats = chatService.listChats(userId);
        return ResponseEntity.ok(chats);
    }

    /**
     * Creates a new chat (direct or group).
     * POST /chats
     *
     * @param input the chat creation input data containing type, participantIds, name, and owner
     * @return 201 Created with the created chat object
     */
    @PostMapping
    public ResponseEntity<Chat> create(@RequestBody CreateChatInput input) {
        Chat chat = chatService.createChat(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(chat);
    }

    /**
     * Retrieves a specific chat by ID.
     * GET /chats/{id}
     *
     * @param idHex the chat ID in hexadecimal format (24-character ObjectId)
     * @return 200 OK with the chat if found, or 404 Not Found if chat doesn't exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<Chat> get(@PathVariable("id") String idHex) {
        ObjectId chatId = new ObjectId(idHex);
        return chatService.getChat(chatId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Adds a participant to an existing chat.
     * POST /chats/{id}/participants
     *
     * @param idHex the chat ID in hexadecimal format (24-character ObjectId)
     * @param body request body containing the userId to add
     * @return 200 OK with the updated chat if successful, or 404 Not Found if chat doesn't exist
     */
    @PostMapping("/{id}/participants")
    public ResponseEntity<Chat> addParticipant(@PathVariable("id") String idHex,
                                                @RequestBody Map<String, String> body) {
        ObjectId chatId = new ObjectId(idHex);
        ObjectId userId = new ObjectId(body.get("userId"));
        return chatService.addParticipant(chatId, userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Sets a role (admin or member) for a user in a chat.
     * POST /chats/{id}/roles
     *
     * @param idHex the chat ID in hexadecimal format (24-character ObjectId)
     * @param body request body containing userId and role (admin or member)
     * @return 200 OK with the updated chat if successful, or 404 Not Found if chat doesn't exist
     */
    @PostMapping("/{id}/roles")
    public ResponseEntity<Chat> setRole(@PathVariable("id") String idHex,
                                        @RequestBody Map<String, String> body) {
        ObjectId chatId = new ObjectId(idHex);
        ObjectId userId = new ObjectId(body.get("userId"));
        Role role = Role.valueOf(body.get("role"));
        return chatService.setRole(chatId, userId, role)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
