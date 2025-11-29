package com.privychat.controller;

import com.privychat.model.Chat;
import com.privychat.model.RoleEntry;
import com.privychat.model.enums.ChatType;
import com.privychat.model.enums.Role;
import com.privychat.model.input.CreateChatInput;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChatsControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void clean() {
        mongoTemplate.getDb().getCollection("chats").drop();
    }

    @Test
    @DisplayName("GET /chats lists chats for a user")
    void listChats() {
        ObjectId userId = new ObjectId();
        Chat c = new Chat();
        c.setType(ChatType.group);
        c.setParticipants(new ArrayList<>(List.of(userId)));
        mongoTemplate.save(c, "chats");

        String url = "/chats?userId=" + userId.toHexString();
        ResponseEntity<Chat[]> res = restTemplate.withBasicAuth("admin", "privychat")
                .getForEntity(url, Chat[].class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).isNotNull();
        assertThat(res.getBody().length).isGreaterThanOrEqualTo(1);
        assertThat(res.getBody()[0].getType()).isEqualTo(ChatType.group);
    }

    @Test
    @DisplayName("POST /chats creates a chat and persists it")
    void createChat() {
        CreateChatInput input = new CreateChatInput();
        input.setType(ChatType.group);
        input.setParticipantIds(List.of(new ObjectId()));
        input.setName("General");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth("admin", "privychat");
        HttpEntity<CreateChatInput> entity = new HttpEntity<>(input, headers);

        ResponseEntity<Chat> res = restTemplate.postForEntity("/chats", entity, Chat.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(res.getBody()).isNotNull();
        assertThat(res.getBody().getType()).isEqualTo(ChatType.group);

        Chat saved = mongoTemplate.findById(res.getBody().getId(), Chat.class, "chats");
        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("General");
    }

    @Test
    @DisplayName("GET /chats/{id} returns persisted chat")
    void getChat() {
        Chat c = new Chat();
        c.setType(ChatType.group);
        c.setParticipants(List.of(new ObjectId()));
        mongoTemplate.save(c, "chats");

        ResponseEntity<Chat> res = restTemplate.withBasicAuth("admin", "privychat")
                .getForEntity("/chats/" + c.getId().toHexString(), Chat.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).isNotNull();
        assertThat(res.getBody().getId()).isEqualTo(c.getId());
    }

    @Test
    @DisplayName("POST /chats/{id}/participants adds a participant")
    void addParticipant() {
        Chat c = new Chat();
        c.setType(ChatType.group);
        c.setParticipants(new ArrayList<>());
        mongoTemplate.save(c, "chats");

        ObjectId newUserId = new ObjectId();
        Map<String, String> body = Map.of("userId", newUserId.toHexString());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth("admin", "privychat");
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Chat> res = restTemplate.postForEntity("/chats/" + c.getId().toHexString() + "/participants", entity, Chat.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);

        Chat updated = mongoTemplate.findById(c.getId(), Chat.class, "chats");
        assertThat(updated.getParticipants()).contains(newUserId);
    }

    @Test
    @DisplayName("POST /chats/{id}/roles sets role for a user and persists")
    void setRole() {
        Chat c = new Chat();
        c.setType(ChatType.group);
        c.setParticipants(new ArrayList<>());
        mongoTemplate.save(c, "chats");

        ObjectId targetUser = new ObjectId();
        Map<String, String> body = Map.of("userId", targetUser.toHexString(), "role", Role.admin.name());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth("admin", "privychat");
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Chat> res = restTemplate.postForEntity("/chats/" + c.getId().toHexString() + "/roles", entity, Chat.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);

        Chat updated = mongoTemplate.findById(c.getId(), Chat.class, "chats");
        assertThat(updated.getRoles()).contains(new RoleEntry(targetUser, Role.admin));
    }
}