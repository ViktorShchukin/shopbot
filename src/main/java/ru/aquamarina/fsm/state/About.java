package ru.aquamarina.fsm.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.fsm.form.AboutForm;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.model.command.Command;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.util.Result;

public class About implements FsmState {

    public static final String NAME = "About";

    private final Logger log = LoggerFactory.getLogger(Index.class);

//    private final Update update;
//    private final AbsSender sender;
//
//    public About(Update update, AbsSender sender) {
////        this.update = update;
//        this.sender = sender;
//    }

//    @Override
//    public Optional<FsmState> doWork(FsmContextHolder context, Update update) {
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
//        return Optional.empty();
//    }


    @Override
    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
        // todo end realization
        return Result.ok(new Index());
    }

    @Override
    public Form getForm() {
        // todo
        return new AboutForm();
    }

    @Override
    public String toString() {
        return NAME;
    }
}
