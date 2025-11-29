package com.privychat.repository;

import com.privychat.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for managing User entities in MongoDB.
 */
@Repository
public class UserRepository {
    private final MongoTemplate mongoTemplate;

    public UserRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Saves a user entity to the database.
     *
     * @param user the user to save
     * @return the saved user with generated ID if it was new
     */
    public User save(User user) {
        return mongoTemplate.save(user);
    }

    /**
     * Finds a user by their ID.
     *
     * @param id the user ID
     * @return an Optional containing the user if found
     */
    public Optional<User> findById(ObjectId id) {
        return Optional.ofNullable(mongoTemplate.findById(id, User.class));
    }

    /**
     * Finds a user by their username.
     *
     * @param username the username to search for
     * @return an Optional containing the user if found
     */
    public Optional<User> findByUsername(String username) {
        Query query = new Query(Criteria.where("username").is(username));
        return Optional.ofNullable(mongoTemplate.findOne(query, User.class));
    }
}
