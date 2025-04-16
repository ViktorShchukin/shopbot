package ru.aquamarina.api.bot.telegram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.aquamarina.api.bot.View;
import ru.aquamarina.fsm.form.AboutForm;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.fsm.form.IndexForm;
import ru.aquamarina.model.error.Error;

public class TelegramView implements View<TelegramDrawContext> {

    private final Logger log = LoggerFactory.getLogger(TelegramView.class);

    private final OkHttpTelegramClient client;

    public TelegramView(OkHttpTelegramClient client) {
        this.client = client;
    }

    @Override
    public void draw(TelegramDrawContext drawContext, Form form) {
        String chatId = drawContext.chatId();
        InlineKeyboardRow keyboardRow = new InlineKeyboardRow();
        form.getCommands().forEach(command -> {
            keyboardRow.add(getButton(command, command));
        });

        String messageText = switch (form) {
            case IndexForm index -> "Привет. Чего желаете";
            case AboutForm index -> "Я есть магазин";
        };

        var keyBoard = InlineKeyboardMarkup.builder()
                .keyboardRow(keyboardRow)
                .build();
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(messageText)
                .replyMarkup(keyBoard)
                .build();

        try {
            log.info("=== try to send message ===");
            Message res = client.execute(message);
            log.info("=== send message: {} ===", res);
        } catch (TelegramApiException e) {
            log.error("some err", e);
        }
    }

    @Override
    public void drawError(TelegramDrawContext drawContext, Error error) {
        // todo implement this
        log.error("=== error inside the app: {}", error.toString());
    }

    private InlineKeyboardButton getButton(String text, String command) {
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(command)
                .build();
    }
}
