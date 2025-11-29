package com.privychat.repository;

import com.privychat.model.Message;
import com.privychat.model.enums.DeliveryStatus;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MessageRepositoryTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MessageRepository repo;

    @BeforeEach
    void clean() {
        mongoTemplate.getDb().getCollection("messages").drop();
    }

    @Test
    void save_setsIdWhenMissing_andPersistsToMessagesCollection() {
        Message msg = new Message();
        msg.setChatId(new ObjectId());
        msg.setSenderId(new ObjectId());
        msg.setBody("Hello");
        msg.setDeliveryStatus(DeliveryStatus.sent);
        msg.setServerTimestamp(Instant.now());

        Message saved = repo.save(msg);
        assertThat(saved.getId()).isNotNull();

        Message raw = mongoTemplate.findById(saved.getId(), Message.class, "messages");
        assertThat(raw).isNotNull();
        assertThat(raw.getBody()).isEqualTo("Hello");
    }

    @Test
    void findById_returnsInsertedDocument() {
        Message m = new Message();
        ObjectId id = new ObjectId();
        m.setId(id);
        m.setChatId(new ObjectId());
        m.setSenderId(new ObjectId());
        m.setBody("Ping");
        m.setDeliveryStatus(DeliveryStatus.sent);
        m.setServerTimestamp(Instant.now());
        mongoTemplate.save(m, "messages");

        Optional<Message> res = repo.findById(id);
        assertThat(res).isPresent();
        assertThat(res.get().getBody()).isEqualTo("Ping");
    }

    @Test
    void findByChat_appliesLimit_andSortsByTimestampAsc() {
        ObjectId chatId = new ObjectId();
        // insert 3 messages with increasing timestamps
        Message m1 = new Message();
        m1.setChatId(chatId);
        m1.setSenderId(new ObjectId());
        m1.setBody("1");
        m1.setDeliveryStatus(DeliveryStatus.sent);
        m1.setServerTimestamp(Instant.parse("2025-01-01T10:00:00Z"));
        Message m2 = new Message();
        m2.setChatId(chatId);
        m2.setSenderId(new ObjectId());
        m2.setBody("2");
        m2.setDeliveryStatus(DeliveryStatus.sent);
        m2.setServerTimestamp(Instant.parse("2025-01-01T10:01:00Z"));
        Message m3 = new Message();
        m3.setChatId(chatId);
        m3.setSenderId(new ObjectId());
        m3.setBody("3");
        m3.setDeliveryStatus(DeliveryStatus.sent);
        m3.setServerTimestamp(Instant.parse("2025-01-01T10:02:00Z"));
        mongoTemplate.save(m1, "messages");
        mongoTemplate.save(m2, "messages");
        mongoTemplate.save(m3, "messages");

        // request with large limit, expect cap to 200 and ascending order
        List<Message> res = repo.findByChat(chatId, 500);
        assertThat(res).hasSize(3);
        assertThat(res.get(0).getBody()).isEqualTo("1");
        assertThat(res.get(1).getBody()).isEqualTo("2");
        assertThat(res.get(2).getBody()).isEqualTo("3");
    }
}
