package com.privychat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.privychat.model.User;
import com.privychat.model.input.CreateUserInput;
import com.privychat.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UsersController.class)
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /users returns 201 with created user")
    void registerUser() throws Exception {
        User created = new User();
        created.setUsername("alice");
        created.setPublicKey("pk");
        created.setCreatedAt(Instant.now());
        Mockito.when(userService.register(any(CreateUserInput.class))).thenReturn(created);

        CreateUserInput input = new CreateUserInput("alice", "secret", "pk", "https://x");

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.username").value("alice"));
    }

    @Test
    @DisplayName("GET /users/{username} returns 200 when found, 404 otherwise")
    void getUserByUsername() throws Exception {
        User alice = new User();
        alice.setUsername("alice");
        Mockito.when(userService.findByUsername(eq("alice"))).thenReturn(Optional.of(alice));
        Mockito.when(userService.findByUsername(eq("missing"))).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/alice"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("alice"));

        mockMvc.perform(get("/users/missing"))
            .andExpect(status().isNotFound());
    }
}