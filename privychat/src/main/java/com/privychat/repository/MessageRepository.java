package com.privychat.repository;

import com.privychat.model.Message;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MessageRepository {
    private static final String COLLECTION = "messages";
    private final MongoTemplate mongoTemplate;

    @Autowired
    public MessageRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Message save(Message msg) {
        if (msg.getId() == null) {
            msg.setId(new ObjectId());
        }
        return mongoTemplate.save(msg, COLLECTION);
    }

    public Optional<Message> findById(ObjectId id) {
        return Optional.ofNullable(mongoTemplate.findById(id, Message.class, COLLECTION));
    }

    public List<Message> findByChat(ObjectId chatId, int limit) {
        int bounded = limit < 1 ? 1 : (limit > 200 ? 200 : limit);
        Query q = new Query(Criteria.where("chatId").is(chatId))
                .limit(bounded)
                .with(Sort.by(Sort.Direction.ASC, "serverTimestamp"));
        return mongoTemplate.find(q, Message.class, COLLECTION);
    }
}
