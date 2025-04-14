package ru.aquamarina.service;

import jakarta.inject.Singleton;
import ru.aquamarina.util.Result;
import ru.aquamarina.model.entity.TelegramInfo;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.UserNotFound;
import ru.aquamarina.repository.TelegramInfoRepository;

@Singleton
public class TelegramInfoService {

    private final TelegramInfoRepository telegramInfoRepository;
    private final UserService userService;

    public TelegramInfoService(TelegramInfoRepository userTelegramInfoRepository, UserService userService) {
        this.telegramInfoRepository = userTelegramInfoRepository;
        this.userService = userService;
    }

    public boolean existsById(Long userId) {
        return telegramInfoRepository.existsById(userId);
    }

    public void save(TelegramInfo telegramInfo) {
        telegramInfoRepository.save(telegramInfo);
    }

    public Result<User, Error> getUserByTelegramId(long id) {
        return telegramInfoRepository
                .getUserIdByTelegramId(id)
                .flatMap(userService::getUser)
                .map(Result::<User, Error>ok)
                .orElseGet(() -> Result.error(new UserNotFound()));
    }
}
