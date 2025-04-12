package ru.aquamarina.fsm;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.aquamarina.model.TelegramInfo;

import java.util.Optional;

public class Init implements FsmState {



    @Override
    public Optional<FsmState> doWork(FsmContextHolder context, Update update) {
        Long userId = update.getMessage().getFrom().getId();
        TelegramInfo nTelegramUser = new TelegramInfo();
        nTelegramUser.setTelegram_id(userId);
//        nTelegramUser.setLast_state("init");
        if (!context.getTelegramInfoService().existsById(userId)){
            context.getTelegramInfoService().save(nTelegramUser);
        }
        return Optional.of(new Start());
    }
}
