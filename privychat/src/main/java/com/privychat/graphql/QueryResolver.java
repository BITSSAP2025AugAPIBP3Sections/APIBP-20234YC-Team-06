package com.privychat.graphql;

import com.privychat.model.Chat;
import com.privychat.model.Message;
import com.privychat.model.User;
import org.bson.types.ObjectId;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
public class QueryResolver {
    private final WebClient client;

    public QueryResolver(WebClient restApiWebClient) {
        this.client = restApiWebClient;
    }

    @QueryMapping
    public User userByUsername(@Argument String username) {
        return client.get()
                .uri("/users/{username}", username)
                .retrieve()
                .onStatus(status -> status.value() == 404, resp -> Mono.empty())
                .bodyToMono(User.class)
                .onErrorResume(e -> Mono.empty())
                .block();
    }

    @QueryMapping
    public List<Chat> chats(@Argument ObjectId userId) {
        return client.get()
                .uri(uriBuilder -> uriBuilder.path("/chats").queryParam("userId", userId.toHexString()).build())
                .retrieve()
                .onStatus(status -> status.value() == 401 || status.value() == 403, resp -> Mono.empty())
                .bodyToFlux(Chat.class)
                .collectList()
                .onErrorReturn(List.of())
                .block();
    }

    @QueryMapping
    public Chat chat(@Argument ObjectId id) {
        return client.get()
                .uri("/chats/{id}", id.toHexString())
                .retrieve()
                .onStatus(status -> status.value() == 404, resp -> Mono.empty())
                .bodyToMono(Chat.class)
                .onErrorResume(e -> Mono.empty())
                .block();
    }

    @QueryMapping
    public List<Message> messages(@Argument ObjectId chatId) {
        return client.get()
                .uri(uriBuilder -> uriBuilder.path("/messages").queryParam("chatId", chatId.toHexString()).build())
                .retrieve()
                .onStatus(status -> status.value() == 401 || status.value() == 403, resp -> Mono.empty())
                .bodyToFlux(Message.class)
                .collectList()
                .onErrorReturn(List.of())
                .block();
    }
}
