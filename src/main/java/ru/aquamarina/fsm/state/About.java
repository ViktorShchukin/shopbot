package ru.aquamarina.fsm.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.aquamarina.fsm.Form;
import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.fsm.FsmState;

import java.util.Optional;

public class About implements FsmState {

    private final Logger log = LoggerFactory.getLogger(Start.class);

//    private final Update update;
//    private final AbsSender sender;
//
//    public About(Update update, AbsSender sender) {
////        this.update = update;
//        this.sender = sender;
//    }

    @Override
    public Optional<FsmState> doWork(FsmContextHolder context, Update update) {
//        var chatId = update.getCallbackQuery().getFrom().getId().toString();
//        var button = InlineKeyboardButton.builder()
//                .text("Назад")
//                .callbackData("index")
//                .build();
//        var keyBoard = InlineKeyboardMarkup.builder()
//                .keyboardRow(List.of(button))
//                .build();
//        AnswerCallbackQuery close = AnswerCallbackQuery.builder()
//                .callbackQueryId(update.getCallbackQuery().getId())
//                .build();
//        SendMessage message = SendMessage.builder()
//                .chatId(chatId)
//                .text("Я есть магазин")
//                .replyMarkup(keyBoard)
//                .build();
//
//        try {
//            log.info("=== callback id: {}", update.getCallbackQuery().getId());
//            Serializable closeRes = sender.execute(close);
//            log.info("===after close {}", closeRes.toString());
//            sender.execute(message);
//        } catch (TelegramApiException e) {
//            log.error("some err", e);
//        }
        return Optional.empty();
    }

    @Override
    public Form getForm() {
        // todo
        return null;
    }
}
