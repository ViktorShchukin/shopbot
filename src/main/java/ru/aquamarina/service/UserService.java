package ru.aquamarina.service;

import jakarta.inject.Singleton;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Singleton
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    Optional<User> getUser(UUID id){
        return userRepository.findById(id);
    }
}
