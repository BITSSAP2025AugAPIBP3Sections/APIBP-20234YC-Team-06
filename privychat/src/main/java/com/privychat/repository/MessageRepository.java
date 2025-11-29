package com.privychat.repository;

import com.privychat.model.Message;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public class MessageRepository {
    private static final int MIN_LIMIT = 1;
    private static final int MAX_LIMIT = 200;

    private final MongoTemplate mongoTemplate;

    public MessageRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Saves a message entity to the database.
     *
     * @param message the message to save
     * @return the saved message with generated ID if it was new
     */
    public Message save(Message message) {
        return mongoTemplate.save(message);
    }

    /**
     * Finds a message by its ID.
     *
     * @param id the message ID
     * @return an Optional containing the message if found
     */
    public Optional<Message> findById(ObjectId id) {
        return Optional.ofNullable(mongoTemplate.findById(id, Message.class));
    }

    /**
     * Finds messages for a specific chat with pagination and sorting.
     * The limit is bounded between 1 and 200 messages.
     *
     * @param chatId the chat ID to search for
     * @param limit the maximum number of messages to return
     * @return list of messages sorted by server timestamp in ascending order
     */
    public List<Message> findByChat(ObjectId chatId, int limit) {
        int boundedLimit = Math.max(MIN_LIMIT, Math.min(limit, MAX_LIMIT));
        Query query = new Query(Criteria.where("chatId").is(chatId))
                .limit(boundedLimit)
                .with(Sort.by(Sort.Direction.ASC, "serverTimestamp"));
        return mongoTemplate.find(query, Message.class);
    }
}
