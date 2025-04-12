package ru.aquamarina.service;

import jakarta.inject.Singleton;
import ru.aquamarina.model.TelegramInfo;
import ru.aquamarina.repository.TelegramInfoRepository;

@Singleton
public class TelegramInfoService {

    private final TelegramInfoRepository telegramInfoRepository;

    public TelegramInfoService(TelegramInfoRepository userTelegramInfoRepository) {
        this.telegramInfoRepository = userTelegramInfoRepository;
    }

    public boolean existsById(Long userId) {
        return telegramInfoRepository.existsById(userId);
    }

    public void save(TelegramInfo telegramInfo) {
        telegramInfoRepository.save(telegramInfo);
    }
}
