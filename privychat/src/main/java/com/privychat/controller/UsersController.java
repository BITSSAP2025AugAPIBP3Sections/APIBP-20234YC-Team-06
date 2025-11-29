package com.privychat.controller;

import com.privychat.model.User;
import com.privychat.model.input.CreateUserInput;
import com.privychat.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsersController {
    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers a new user.
     * POST /users
     *
     * @param input the user registration data containing username, password, publicKey, and avatarUrl
     * @return 201 Created with the created user object
     */
    @PostMapping
    public ResponseEntity<User> register(@RequestBody CreateUserInput input) {
        User user = userService.register(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    /**
     * Retrieves a user by their username.
     * GET /users/{username}
     *
     * @param username the username to search for
     * @return 200 OK with the user if found, or 404 Not Found if user doesn't exist
     */
    @GetMapping("/{username}")
    public ResponseEntity<User> getByUsername(@PathVariable String username) {
        return userService.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
