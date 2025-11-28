package com.privychat.repository;

import com.privychat.model.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository {
    private static final String COLLECTION = "users"; // plural to align with changelog
    private final MongoTemplate mongoTemplate;

    @Autowired
    public UserRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public User save(User user) {
        // Ensure id is set
        if (user.getId() == null) {
            user.setId(new ObjectId());
        }
        return mongoTemplate.save(user, COLLECTION);
    }

    public Optional<User> findById(ObjectId id) {
        return Optional.ofNullable(mongoTemplate.findById(id, User.class, COLLECTION));
    }

    public Optional<User> findByUsername(String username) {
        Query q = new Query(Criteria.where("username").is(username));
        return Optional.ofNullable(mongoTemplate.findOne(q, User.class, COLLECTION));
    }
}
