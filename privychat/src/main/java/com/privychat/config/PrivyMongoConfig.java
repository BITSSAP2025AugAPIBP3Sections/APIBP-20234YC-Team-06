package com.privychat.config;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@EnableConfigurationProperties(PrivyMongoProperties.class)
public class PrivyMongoConfig {

    @Bean
    public MongoClient mongoClient(PrivyMongoProperties props) {
        if (props.getUri() != null && !props.getUri().isBlank()) {
            return MongoClients.create(props.getUri());
        }
        // Build URI from pieces when full URI isn't provided
        String username = props.getUsername();
        String password = props.getPassword();
        String host = props.getHost();
        int port = props.getPort();
        String database = props.getDatabase() != null ? props.getDatabase() : "privychat";
        String authSource = props.getAuthSource() != null ? props.getAuthSource() : "admin";

        // URL-encode special characters in password minimally for '@'
        if (password != null) {
            password = password.replace("@", "%40");
        }

        String uri = String.format("mongodb://%s:%s@%s:%d/%s?authSource=%s", username, password, host, port, database, authSource);
        return MongoClients.create(new ConnectionString(uri));
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient, PrivyMongoProperties props) {
        String database = props.getDatabase() != null ? props.getDatabase() : "privychat";
        return new MongoTemplate(mongoClient, database);
    }
}

