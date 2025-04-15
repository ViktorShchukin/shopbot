package ru.aquamarina.fsm.state;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.model.command.Command;
import ru.aquamarina.model.entity.TelegramInfo;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.util.Result;

import java.util.Optional;

public class Init implements FsmState {

    public static final String NAME = "Init";

//    @Override
//    public Optional<FsmState> doWork(FsmContextHolder context, Update update) {
//        Long userId = update.getMessage().getFrom().getId();
//        TelegramInfo nTelegramUser = new TelegramInfo();
//        nTelegramUser.setTelegram_id(userId);
////        nTelegramUser.setLast_state("init");
//        if (!context.getTelegramInfoService().existsById(userId)){
//            context.getTelegramInfoService().save(nTelegramUser);
//        }
//        return Optional.of(new Start());
//    }


    @Override
    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
        return Result.ok(new Start());
    }

    @Override
    public Form getForm() {
        // todo
        return null;
    }
}
