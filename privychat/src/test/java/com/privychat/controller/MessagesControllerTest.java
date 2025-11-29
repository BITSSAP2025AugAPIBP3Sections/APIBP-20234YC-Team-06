package com.privychat.controller;

import com.privychat.model.AckResponse;
import com.privychat.model.Message;
import com.privychat.model.enums.DeliveryStatus;
import com.privychat.model.input.SendMessageInput;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MessagesControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void clean() {
        mongoTemplate.getDb().getCollection("messages").drop();
    }

    @Test
    @DisplayName("GET /messages lists messages by chat")
    void listMessages() {
        // Seed a message
        ObjectId chatId = new ObjectId();
        Message m = new Message();
        m.setChatId(chatId);
        m.setSenderId(new ObjectId());
        m.setBody("Hello");
        m.setDeliveryStatus(DeliveryStatus.sent);
        m.setServerTimestamp(Instant.now());
        mongoTemplate.save(m, "messages");

        String url = "/messages?chatId=" + chatId.toHexString();
        ResponseEntity<Message[]> res = restTemplate.withBasicAuth("admin", "privychat")
                .getForEntity(url, Message[].class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).isNotNull();
        assertThat(res.getBody().length).isGreaterThanOrEqualTo(1);
        assertThat(res.getBody()[0].getBody()).isEqualTo("Hello");
    }

    @Test
    @DisplayName("POST /messages creates a message")
    void sendMessage() {
        // Prepare input
        SendMessageInput input = new SendMessageInput();
        input.setChatId(new ObjectId());
        input.setBody("Hello");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth("admin", "privychat");
        headers.add("X-User-Id", new ObjectId().toHexString());
        HttpEntity<SendMessageInput> entity = new HttpEntity<>(input, headers);

        ResponseEntity<Message> res = restTemplate.postForEntity("/messages", entity, Message.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(res.getBody()).isNotNull();
        assertThat(res.getBody().getBody()).isEqualTo("Hello");
        // Verify persisted
        Message saved = mongoTemplate.findById(res.getBody().getId(), Message.class, "messages");
        assertThat(saved).isNotNull();
    }

    @Test
    @DisplayName("POST /messages/{id}/ack acknowledges a message")
    void acknowledgeMessage() {
        // Seed a message
        Message m = new Message();
        m.setChatId(new ObjectId());
        m.setSenderId(new ObjectId());
        m.setBody("Ping");
        m.setDeliveryStatus(DeliveryStatus.sent);
        m.setServerTimestamp(Instant.now());
        mongoTemplate.save(m, "messages");

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("admin", "privychat");
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<AckResponse> res = restTemplate.postForEntity("/messages/" + m.getId().toHexString() + "/ack", entity, AckResponse.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).isNotNull();
        assertThat(res.getBody().isAcknowledged()).isTrue();
        // Verify status updated
        Message updated = mongoTemplate.findById(m.getId(), Message.class, "messages");
        assertThat(updated.getDeliveryStatus()).isEqualTo(DeliveryStatus.delivered);
    }
}