package ru.aquamarina.fsm.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.model.command.Command;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.util.Result;

import java.util.List;
import java.util.Optional;

public class Start implements FsmState {

    public static final String NAME = "Start";

    private final Logger log = LoggerFactory.getLogger(Start.class);

//    @Override
//    public Optional<FsmState> doWork(FsmContextHolder context, Update update) {
//        String chatId;
//        if (update.hasMessage()) {
//            chatId = update.getMessage().getChatId().toString();
//        } else {
//            chatId = update.getCallbackQuery().getMessage().getChatId().toString();
//        }
////        var chatId = ;
//        var button = InlineKeyboardButton.builder()
//                .text("О нас")
//                .callbackData("about")
//                .build();
//        var button1 = InlineKeyboardButton.builder()
//                .text("Каталог")
//                .callbackData("catalog")
//                .build();
//        InlineKeyboardRow keyboardRow = new InlineKeyboardRow(List.of(button, button1));
//        var keyBoard = InlineKeyboardMarkup.builder()
//                .keyboardRow(keyboardRow)
//                .build();
//        SendMessage message = SendMessage.builder()
//                .chatId(chatId)
//                .text("Привет. Чего желаете")
//                .replyMarkup(keyBoard)
//                .build();
//
//        try {
//            context.getTelegramClient().execute(message);
//        } catch (TelegramApiException e) {
//            log.error("some err", e);
//        }
//        return Optional.empty();
//    }


    @Override
    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
        return Result.ok(new About());
    }

    @Override
    public Form getForm() {
        // todo
        return null;
    }
}
