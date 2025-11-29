package com.privychat.repository;

import com.privychat.model.Chat;
import com.privychat.model.enums.ChatType;
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
class ChatRepositoryTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ChatRepository repo;

    @BeforeEach
    void clean() {
        mongoTemplate.getDb().getCollection("chats").drop();
    }

    @Test
    void save_setsIdWhenMissing_andPersistsToChatsCollection() {
        Chat chat = new Chat();
        chat.setType(ChatType.group);
        chat.setName("General");
        chat.setParticipants(List.of(new ObjectId(), new ObjectId()));
        chat.setCreatedAt(Instant.now());

        Chat saved = repo.save(chat);
        assertThat(saved.getId()).isNotNull();

        Chat raw = mongoTemplate.findById(saved.getId(), Chat.class, "chats");
        assertThat(raw).isNotNull();
        assertThat(raw.getName()).isEqualTo("General");
        assertThat(raw.getParticipants()).hasSize(2);
    }

    @Test
    void findById_returnsInsertedDocument() {
        Chat chat = new Chat();
        ObjectId id = new ObjectId();
        chat.setId(id);
        chat.setType(ChatType.direct);
        chat.setParticipants(List.of(new ObjectId()));
        mongoTemplate.save(chat, "chats");

        Optional<Chat> res = repo.findById(id);
        assertThat(res).isPresent();
        assertThat(res.get().getType()).isEqualTo(ChatType.direct);
    }

    @Test
    void findByParticipant_returnsMatchingChats() {
        ObjectId userId = new ObjectId();
        Chat c1 = new Chat();
        c1.setId(new ObjectId());
        c1.setParticipants(List.of(userId));
        Chat c2 = new Chat();
        c2.setId(new ObjectId());
        c2.setParticipants(List.of(new ObjectId()));
        mongoTemplate.save(c1, "chats");
        mongoTemplate.save(c2, "chats");

        List<Chat> res = repo.findByParticipant(userId);
        assertThat(res).hasSize(1);
        assertThat(res.get(0).getId()).isEqualTo(c1.getId());
    }
}
