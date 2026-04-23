package ru.aquamarina.service;

import io.micronaut.data.exceptions.DataAccessException;
import io.micronaut.validation.Validated;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.model.UserRole;
import ru.aquamarina.model.error.IoError;
import ru.aquamarina.util.Result;
import ru.aquamarina.model.entity.TelegramInfo;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.UserNotFound;

import java.util.List;
import java.util.UUID;

@Validated
@Singleton
public class TelegramInfoService {

    private final Logger log = LoggerFactory.getLogger(TelegramInfoService.class);

    private final TelegramInfoServiceWithExc telegramInfoServiceWithExc;

    public TelegramInfoService(TelegramInfoServiceWithExc telegramInfoServiceWithExc) {
        this.telegramInfoServiceWithExc = telegramInfoServiceWithExc;
    }

    public boolean existsById(Long userId) {
        return telegramInfoServiceWithExc.existsById(userId);
    }

    public Result<TelegramInfo, Error> create(@NotNull long telegramId,
                                              @NotNull UUID userId,
                                              String firstName,
                                              String lastName,
                                              String userName,
                                              Boolean updated,
                                              Integer lastMessageId) {
        // todo get rid of try-catch block by creating special method in Result. Maybe???
        try {
            return telegramInfoServiceWithExc.create(telegramId,
                    userId,
                    firstName,
                    lastName,
                    userName,
                    updated,
                    lastMessageId);
        } catch (DataAccessException e) {
            return Result.error(new IoError(e));
        }
    }

    public Result<TelegramInfo, Error> update(Long telegramId,
                                              String firstName,
                                              String lastName,
                                              String userName,
                                              Integer lastMessageId) {
        try {
            return telegramInfoServiceWithExc.update(telegramId, firstName, lastName, userName, lastMessageId);
        } catch (DataAccessException e) {
            return Result.error(new IoError(e));
        }
    }

    public Result<TelegramInfo, Error> getByUser(User user) {
        try {
            return telegramInfoServiceWithExc.getByUser(user);
        } catch (DataAccessException e) {
            return Result.error(new IoError(e));
        }
    }

    public Result<User, Error> getOrCrateUserByTelegramId(long telegramId) {
        try {
            return telegramInfoServiceWithExc.getOrCrateUserByTelegramId(telegramId);
        } catch (DataAccessException e) {
            return Result.error(new IoError(e));
        }
    }

    /**
     * @param telegramId
     * @return {@link UserNotFound}
     */
    public Result<User, Error> getUserByTelegramId(long telegramId) {
        try {
            return telegramInfoServiceWithExc.getUserByTelegramId(telegramId);
        } catch (DataAccessException e) {
            return Result.error(new IoError(e));
        }
    }

    public List<TelegramInfo> getByUserRole(UserRole userRole) {
        return telegramInfoServiceWithExc.getByUserRole(userRole);
    }


    public void updateSource(User user, String source) {
        try {
            telegramInfoServiceWithExc.updateSource(user, source);
        } catch (DataAccessException e) {
            log.error("error during source update user: {}, source: {}", user, source);
        }
    }
}
