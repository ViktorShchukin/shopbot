package ru.aquamarina.fsm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Optional;

public class UnknownCommand implements FsmState{

    private final Logger log = LoggerFactory.getLogger(UnknownCommand.class);

    @Override
    public Optional<FsmState> doWork(FsmContextHolder context, Update update) {
        String chatId;
        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId().toString();
        } else {
            chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        }
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text("Неизвестная команда")
                .build();

        try {
            context.getTelegramClient().execute(message);
        } catch (TelegramApiException e) {
            // todo check all error messages
            log.error("some err", e);
        }
        return Optional.empty();
    }
}
