package ru.aquamarina.service;

import io.micronaut.data.exceptions.DataAccessException;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.fsm.state.FsmState;
import ru.aquamarina.model.UserRole;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.IoError;
import ru.aquamarina.util.Result;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserServiceWithExc userServiceWithExc;

    public UserService(UserServiceWithExc userServiceWithExc) {
        this.userServiceWithExc = userServiceWithExc;
    }

    public Optional<User> getUser(UUID id) {
        // todo redo to return result with UserNotFound.
        return userServiceWithExc.getUser(id);
    }

    public Result<User, Error> create(String login, UserRole userRole) {
        try {
            return userServiceWithExc.create(login, userRole);
        } catch (DataAccessException e) {
            return Result.error(new IoError(e));
        }
    }

    public Result<FsmState, Error> updateState(User user, FsmState state) {
        try{
            // todo think about is it normal to return state instead of user.
            return userServiceWithExc.updateState(user, state);
        } catch (DataAccessException e) {
            return Result.error(new IoError(e));
        }
    }

    public List<User> getAllByUserRole(UserRole userRole) {
        return userServiceWithExc.getAllByUserRole(userRole);
    }
}
