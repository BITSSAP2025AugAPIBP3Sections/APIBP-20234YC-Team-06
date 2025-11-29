package com.privychat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Configuration
public class GraphQLWebClientConfig {
    @Bean
    public WebClient restApiWebClient() {
        String user = System.getenv().getOrDefault("PRIVYCHAT_ADMIN_USER", "admin");
        String pass = System.getenv().getOrDefault("PRIVYCHAT_ADMIN_PASSWORD", "privychat");
        String basicToken = Base64.getEncoder().encodeToString((user + ":" + pass).getBytes(StandardCharsets.UTF_8));
        return WebClient.builder()
                .baseUrl("http://localhost:8080")
                .defaultHeader("Authorization", "Basic " + basicToken)
                .build();
    }
}
