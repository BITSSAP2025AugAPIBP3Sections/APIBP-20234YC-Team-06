package com.privychat.service;

import com.privychat.model.Chat;
import com.privychat.model.RoleEntry;
import com.privychat.model.enums.Role;
import com.privychat.model.input.CreateChatInput;
import com.privychat.repository.ChatRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChatService {
    private final ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public Chat createChat(CreateChatInput input) {
        Chat chat = new Chat();
        chat.setType(input.getType());
        chat.setParticipants(new ArrayList<>(input.getParticipantIds()));
        chat.setName(input.getName());
        chat.setOwner(input.getOwner());
        chat.setCreatedAt(Instant.now());
        return chatRepository.save(chat);
    }

    public Optional<Chat> getChat(ObjectId id) {
        return chatRepository.findById(id);
    }

    public List<Chat> listChats(ObjectId userId) {
        return chatRepository.findByParticipant(userId);
    }

    public Optional<Chat> addParticipant(ObjectId chatId, ObjectId userId) {
        Optional<Chat> chatOpt = chatRepository.findById(chatId);
        chatOpt.ifPresent(chat -> {
            List<ObjectId> participants = new ArrayList<>(chat.getParticipants());
            if (!participants.contains(userId)) {
                participants.add(userId);
                chat.setParticipants(participants);
                chatRepository.save(chat);
            }
        });
        return chatOpt;
    }

    public Optional<Chat> setRole(ObjectId chatId, ObjectId userId, Role role) {
        Optional<Chat> chatOpt = chatRepository.findById(chatId);
        chatOpt.ifPresent(chat -> {
            List<RoleEntry> roles = new ArrayList<>(chat.getRoles());
            roles.removeIf(r -> r.getUserId().equals(userId));
            roles.add(new RoleEntry(userId, role));
            chat.setRoles(roles);
            chatRepository.save(chat);
        });
        return chatOpt;
    }
}
