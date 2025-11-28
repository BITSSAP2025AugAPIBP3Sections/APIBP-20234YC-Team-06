package com.privychat.service;

import com.privychat.model.User;
import com.privychat.model.input.CreateUserInput;
import org.bson.types.ObjectId;
import com.privychat.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(CreateUserInput input) {
        User user = new User();
        user.setUsername(input.getUsername());
        user.setPublicKey(input.getPublicKey());
        user.setAvatarUrl(input.getAvatarUrl());
        user.setCreatedAt(Instant.now());
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(ObjectId id) {
        return userRepository.findById(id);
    }
}
