package ru.aquamarina.service;

import jakarta.inject.Singleton;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Singleton
public class Bot extends TelegramLongPollingBot {

    private final Logger log = LoggerFactory.getLogger(Bot.class);

    @Override
    public String getBotUsername() {
        return "shopbot";
    }

    @Override
    public String getBotToken() {
        return "7920461898:AAFOOcHsJYIV0UTIdbAuwqcH8C_XDeXuDqw";
    }

    @Override
    public void onUpdateReceived(Update update) {
        String chatId;
        if (update.hasMessage() && update.getMessage().hasText()){
            chatId = update.getMessage().getChatId().toString();
        } else {
            chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        }
        var button = InlineKeyboardButton.builder()
                .text("О нас")
                .callbackData("about")
                .build();
        var keyBoard = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(button))
                .build();
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text("Привет. Чего желаете")
                .replyMarkup(keyBoard)
                .build();

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("some err", e);
        }
    }
}
