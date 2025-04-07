package ru.aquamarina.fsm;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.aquamarina.model.UserTelegramInfo;
import ru.aquamarina.repository.UserTelegramInfoRepository;

import java.util.Optional;

public class Init implements FsmState {

    private final UserTelegramInfoRepository telegramInfoRepository;
    private final AbsSender absSender;

    public Init(UserTelegramInfoRepository telegramInfoRepository, AbsSender absSender) {
        this.telegramInfoRepository = telegramInfoRepository;
        this.absSender = absSender;
    }

    @Override
    public Optional<FsmState> doWork(Update update) {
        Long userId = update.getMessage().getFrom().getId();
        UserTelegramInfo nTelegramUser = new UserTelegramInfo();
        nTelegramUser.setTelegram_id(userId);
//        nTelegramUser.setLast_state("init");
        if (!telegramInfoRepository.existsById(userId)){
            telegramInfoRepository.save(nTelegramUser);
        }
        return Optional.of(new Start(update, absSender));
    }
}
