package com.privychat.repository;

import com.privychat.model.Chat;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public class ChatRepository {
    private final MongoTemplate mongoTemplate;

    public ChatRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Saves a chat entity to the database.
     *
     * @param chat the chat to save
     * @return the saved chat with generated ID if it was new
     */
    public Chat save(Chat chat) {
        return mongoTemplate.save(chat);
    }

    /**
     * Finds a chat by its ID.
     *
     * @param id the chat ID
     * @return an Optional containing the chat if found
     */
    public Optional<Chat> findById(ObjectId id) {
        return Optional.ofNullable(mongoTemplate.findById(id, Chat.class));
    }

    /**
     * Finds all chats where the given user is a participant.
     *
     * @param userId the user ID to search for
     * @return list of chats containing the user as a participant
     */
    public List<Chat> findByParticipant(ObjectId userId) {
        Query query = new Query(Criteria.where("participants").in(userId));
        return mongoTemplate.find(query, Chat.class);
    }
}
