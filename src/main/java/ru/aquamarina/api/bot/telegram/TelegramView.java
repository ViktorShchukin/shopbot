package ru.aquamarina.api.bot.telegram;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.aquamarina.api.bot.DrawContext;
import ru.aquamarina.api.bot.View;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.util.Result;

import java.util.List;

public class TelegramView implements View<TelegramDrawContext> {

    private final OkHttpTelegramClient client;

    public TelegramView(OkHttpTelegramClient client) {
        this.client = client;
    }

    @Override
    public void draw(TelegramDrawContext drawContext, Form form) {
        String chatId = drawContext.getChatId();

        var button = InlineKeyboardButton.builder()
                .text("О нас")
                .callbackData("about")
                .build();
        var button1 = InlineKeyboardButton.builder()
                .text("Каталог")
                .callbackData("catalog")
                .build();
        InlineKeyboardRow keyboardRow = new InlineKeyboardRow(List.of(button, button1));
        var keyBoard = InlineKeyboardMarkup.builder()
                .keyboardRow(keyboardRow)
                .build();
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text("Привет. Чего желаете")
                .replyMarkup(keyBoard)
                .build();

        try {
            context.getTelegramClient().execute(message);
        } catch (TelegramApiException e) {
            log.error("some err", e);
        }
    }

    @Override
    public void drawError(TelegramDrawContext drawContext, Error error) {
    }
}
