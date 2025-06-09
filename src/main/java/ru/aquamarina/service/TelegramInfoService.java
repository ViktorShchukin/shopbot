package ru.aquamarina.service;

import io.micronaut.data.exceptions.DataAccessException;
import io.micronaut.validation.Validated;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.mapper.TelegramInfoUtil;
import ru.aquamarina.model.UserRole;
import ru.aquamarina.model.error.IoError;
import ru.aquamarina.model.error.NotFound;
import ru.aquamarina.util.Result;
import ru.aquamarina.model.entity.TelegramInfo;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.UserNotFound;
import ru.aquamarina.repository.TelegramInfoRepository;
import ru.aquamarina.util.ResultError;
import ru.aquamarina.util.ResultOk;

import java.util.List;
import java.util.UUID;

@Validated
@Singleton
public class TelegramInfoService {

    private final Logger log = LoggerFactory.getLogger(TelegramInfoService.class);

    private final TelegramInfoRepository telegramInfoRepository;
    private final UserService userService;
    private final TelegramInfoUtil telegramInfoUtil;

    public TelegramInfoService(TelegramInfoRepository userTelegramInfoRepository, UserService userService, TelegramInfoUtil telegramInfoUtil) {
        this.telegramInfoRepository = userTelegramInfoRepository;
        this.userService = userService;
        this.telegramInfoUtil = telegramInfoUtil;
    }

    @Transactional
    public boolean existsById(Long userId) {
        return telegramInfoRepository.existsById(userId);
    }

    @Transactional
    public void save(TelegramInfo telegramInfo) {
        telegramInfoRepository.save(telegramInfo);
    }

    @Transactional
    public Result<TelegramInfo, Error> create(@NotNull long telegramId,
                                              @NotNull UUID userId,
                                              String firstName,
                                              String lastName,
                                              String userName,
                                              Boolean updated,
                                              Integer lastMessageId) {
        // todo get rid of try-catch block
        try {
            TelegramInfo newO = telegramInfoUtil.create(
                    telegramId,
                    userId,
                    firstName,
                    lastName,
                    userName,
                    updated,
                    lastMessageId
            );
            return Result.ok(telegramInfoRepository.save(newO));
        } catch (Exception e) {
            return Result.error(new IoError(e));
        }
    }

    @Transactional
    public Result<TelegramInfo, Error> update(Long telegramId,
                                              String firstName,
                                              String lastName,
                                              String userName,
                                              Integer lastMessageId) {
        try {
            return telegramInfoRepository
                    .findById(telegramId)
                    .map(info -> telegramInfoUtil.update(info, firstName, lastName, userName, lastMessageId))
                    .map(telegramInfoRepository::update)
                    .map(Result::<TelegramInfo, Error>ok)
                    .orElseGet(() -> Result.error(new NotFound("this telegram info not found")));
        } catch (DataAccessException e) {
            return Result.error(new IoError(e));
        }
    }

    @Transactional
    public Result<TelegramInfo, Error> getByUser(User user) {
        try {
            return telegramInfoRepository.findByUserId(user.getId())
                    .map(Result::<TelegramInfo, Error>ok)
                    .orElseGet(() -> Result.error(new NotFound("telegram info for this user not found. User Id: %s".formatted(user.getId().toString()))));
        } catch (Exception e) {
            return Result.error(new IoError(e));
        }
    }

    @Transactional
    public Result<User, Error> getOrCrateUserByTelegramId(long telegramId) {
        Result<User, Error> res = getUserByTelegramId(telegramId);
        switch (res) {
            case ResultOk<User, Error> ok -> {
                return ok;
            }
            case ResultError<User, Error> error -> {
                var user = userService.create(null, UserRole.CUSTOMER);
                user.map(usr -> create(telegramId, usr.getId(), null, null, null, false, null));
                // todo not save. Think how to do user.create and telegramInfo.create as transactional operation
                return user;
            }
        }
    }

    /**
     * @param telegramId
     * @return {@link UserNotFound}
     */
    @Transactional
    public Result<User, Error> getUserByTelegramId(long telegramId) {
        // todo get rid of try-catch block
        try {
            return telegramInfoRepository
                    .getUserIdByTelegramId(telegramId)
                    .flatMap(userService::getUser)
                    .map(Result::<User, Error>ok)
                    .orElseGet(() -> Result.error(new UserNotFound()));
        } catch (Exception e) {
            return Result.error(new IoError(e));
        }
    }

    @Transactional
    public List<TelegramInfo> getByUserRole(UserRole userRole) {
        return telegramInfoRepository.getByUserRole(userRole);
    }


}
