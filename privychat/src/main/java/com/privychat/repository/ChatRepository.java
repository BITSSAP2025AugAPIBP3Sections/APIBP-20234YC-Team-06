package com.privychat.repository;

import com.privychat.model.Chat;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ChatRepository {
    private static final String COLLECTION = "chats";
    private final MongoTemplate mongoTemplate;

    @Autowired
    public ChatRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Chat save(Chat chat) {
        if (chat.getId() == null) {
            chat.setId(new ObjectId());
        }
        return mongoTemplate.save(chat, COLLECTION);
    }

    public Optional<Chat> findById(ObjectId id) {
        return Optional.ofNullable(mongoTemplate.findById(id, Chat.class, COLLECTION));
    }

    public List<Chat> findByParticipant(ObjectId userId) {
        Query q = new Query(Criteria.where("participants").in(userId));
        return mongoTemplate.find(q, Chat.class, COLLECTION);
    }
}
