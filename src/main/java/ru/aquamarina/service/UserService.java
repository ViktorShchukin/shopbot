package ru.aquamarina.service;

import io.micronaut.data.exceptions.DataAccessException;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.fsm.state.FsmState;
import ru.aquamarina.fsm.state.Init;
import ru.aquamarina.mapper.UserMapper;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.IoError;
import ru.aquamarina.repository.UserRepository;
import ru.aquamarina.util.Result;

import java.util.Optional;
import java.util.UUID;

@Singleton
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

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
    public Result<User, Error> create(String login) {
        try {
            User user = userMapper.create(login, Init.NAME);
            return Result.ok(userRepository.save(user));
        } catch (Exception e) {
            return Result.error(new IoError(e));
        }
    }

    public Result<FsmState, Error> updateState(User user, FsmState state) {
        try{
            User updated = userMapper.update(user, null, state);
            userRepository.save(updated);
            // todo think about is it normal to return state instead of user.
            return Result.ok(state);
        } catch (DataAccessException e) {
            return Result.error(new IoError(e));
        }
    }
}
