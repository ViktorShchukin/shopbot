package ru.aquamarina.service;

import io.micronaut.validation.Validated;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import ru.aquamarina.mapper.TelegramInfoMapper;
import ru.aquamarina.util.Result;
import ru.aquamarina.model.entity.TelegramInfo;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.UserNotFound;
import ru.aquamarina.repository.TelegramInfoRepository;
import ru.aquamarina.util.ResultError;
import ru.aquamarina.util.ResultOk;

import java.util.UUID;

@Validated
@Singleton
public class TelegramInfoService {

    private final TelegramInfoRepository telegramInfoRepository;
    private final UserService userService;
    private final TelegramInfoMapper telegramInfoMapper;

    public TelegramInfoService(TelegramInfoRepository userTelegramInfoRepository, UserService userService, TelegramInfoMapper telegramInfoMapper) {
        this.telegramInfoRepository = userTelegramInfoRepository;
        this.userService = userService;
        this.telegramInfoMapper = telegramInfoMapper;
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
    public TelegramInfo create(long telegramId,@NotNull UUID userId) {
        return telegramInfoRepository.save(
                telegramInfoMapper.create(telegramId, userId));
    }

    @Transactional
    public Result<User, Error> getOrCrateUserByTelegramId(long telegramId) {
        Result<User, Error> res = getUserByTelegramId(telegramId);
        switch (res) {
            case ResultOk<User, Error> ok -> {
                return ok;
            }
            case ResultError error -> {
                var user = userService.create(null, null);
                create(telegramId, user.getId());
                return Result.ok(user);
            }
        }
    }

    /**
     * @param telegramId
     * @return {@link UserNotFound}
     */
    @Transactional
    public Result<User, Error> getUserByTelegramId(long telegramId) {
        return telegramInfoRepository
                .getUserIdByTelegramId(telegramId)
                .flatMap(userService::getUser)
                .map(Result::<User, Error>ok)
                .orElseGet(() -> Result.error(new UserNotFound()));
    }
}
