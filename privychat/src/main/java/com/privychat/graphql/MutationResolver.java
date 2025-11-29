package com.privychat.graphql;

import com.privychat.model.AckResponse;
import com.privychat.model.Chat;
import com.privychat.model.Message;
import com.privychat.model.User;
import com.privychat.model.enums.Role;
import com.privychat.model.input.CreateChatInput;
import com.privychat.model.input.CreateUserInput;
import com.privychat.model.input.SendMessageInput;
import org.bson.types.ObjectId;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Controller
public class MutationResolver {
    private final WebClient client;

    public MutationResolver(WebClient restApiWebClient) {
        this.client = restApiWebClient;
    }

    @MutationMapping
    public User registerUser(@Argument CreateUserInput input) {
        return client.post()
                .uri("/users")
                .bodyValue(input)
                .retrieve()
                .bodyToMono(User.class)
                .block();
    }

    @MutationMapping
    public Chat createChat(@Argument CreateChatInput input) {
        return client.post()
                .uri("/chats")
                .bodyValue(input)
                .retrieve()
                .bodyToMono(Chat.class)
                .onErrorResume(e -> Mono.empty())
                .block();
    }

    @MutationMapping
    public Chat addChatParticipant(@Argument ObjectId chatId, @Argument ObjectId userId) {
        Map<String, String> body = Map.of("userId", userId.toHexString());
        return client.post()
                .uri("/chats/{id}/participants", chatId.toHexString())
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Chat.class)
                .onErrorResume(e -> Mono.empty())
                .block();
    }

    @MutationMapping
    public Chat setChatUserRole(@Argument ObjectId chatId, @Argument ObjectId userId, @Argument Role role) {
        Map<String, String> body = Map.of("userId", userId.toHexString(), "role", role.name());
        return client.post()
                .uri("/chats/{id}/roles", chatId.toHexString())
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Chat.class)
                .onErrorResume(e -> Mono.empty())
                .block();
    }

    @MutationMapping
    public Message sendMessage(@Argument ObjectId senderId, @Argument SendMessageInput input) {
        return client.post()
                .uri("/messages")
                .header("X-User-Id", senderId.toHexString())
                .bodyValue(input)
                .retrieve()
                .bodyToMono(Message.class)
                .onErrorResume(e -> Mono.empty())
                .block();
    }

    @MutationMapping
    public AckResponse acknowledgeMessage(@Argument ObjectId id) {
        return client.post()
                .uri("/messages/{id}/ack", id.toHexString())
                .retrieve()
                .bodyToMono(AckResponse.class)
                .onErrorResume(e -> Mono.just(new AckResponse(false)))
                .block();
    }
}
