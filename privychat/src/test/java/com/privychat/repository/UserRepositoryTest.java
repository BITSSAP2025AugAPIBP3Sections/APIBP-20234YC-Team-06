package com.privychat.repository;

import com.privychat.model.User;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserRepository repo;

    @BeforeEach
    void clean() {
        mongoTemplate.getDb().getCollection("users").drop();
    }

    @Test
    void save_setsIdWhenMissing_andPersistsToUsersCollection() {
        User user = new User();
        user.setUsername("alice");
        user.setPublicKey("pk");
        user.setCreatedAt(Instant.now());

        User saved = repo.save(user);
        assertThat(saved.getId()).isNotNull();

        User raw = mongoTemplate.findById(saved.getId(), User.class, "users");
        assertThat(raw).isNotNull();
        assertThat(raw.getUsername()).isEqualTo("alice");
    }

    @Test
    void findById_returnsInsertedDocument() {
        User u = new User();
        ObjectId id = new ObjectId();
        u.setId(id);
        u.setUsername("bob");
        u.setPublicKey("pk-bob");
        u.setCreatedAt(Instant.now());
        mongoTemplate.save(u, "users");

        Optional<User> res = repo.findById(id);
        assertThat(res).isPresent();
        assertThat(res.get().getUsername()).isEqualTo("bob");
    }

    @Test
    void findByUsername_returnsInsertedDocument() {
        User u = new User();
        u.setId(new ObjectId());
        u.setUsername("carol");
        u.setPublicKey("pk-carol");
        u.setCreatedAt(Instant.now());
        mongoTemplate.save(u, "users");

        Optional<User> res = repo.findByUsername("carol");
        assertThat(res).isPresent();
        assertThat(res.get().getPublicKey()).isEqualTo("pk-carol");
    }
}
