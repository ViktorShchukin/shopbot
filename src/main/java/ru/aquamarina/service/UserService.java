package ru.aquamarina.service;

import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import ru.aquamarina.mapper.UserMapper;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Singleton
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public Optional<User> getUser(UUID id) {
        // todo redo to return result with UserNotFound.
        return userRepository.findById(id);
    }

    @Transactional
    public User create(String login, String lastState) {
        return userRepository.save(
                userMapper.create(login, lastState));
    }
}
