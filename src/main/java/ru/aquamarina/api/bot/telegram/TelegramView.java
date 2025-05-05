package ru.aquamarina.api.bot.telegram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.aquamarina.api.bot.View;
import ru.aquamarina.fsm.form.*;
import ru.aquamarina.model.command.ProductAboutCmd;
import ru.aquamarina.model.entity.Product;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.util.ResultError;
import ru.aquamarina.util.ResultOk;

import java.util.ArrayList;
import java.util.List;

public record TelegramView(OkHttpTelegramClient client, Update update) implements View {

    private static final Logger log = LoggerFactory.getLogger(TelegramView.class);

    @Override
    public void drawAboutForm(AboutForm form) {
        String chatId;
        switch (TelegramUtils.extractTelegramUserId(update)) {
            case ResultOk<Long, Error> ok -> chatId = ok.unwrap().toString();
            case ResultError<Long, Error> err -> {
                draw(err.err());
                return;
            }
        }
        InlineKeyboardRow keyboardRow = new InlineKeyboardRow();
        form.getCommands().forEach(command -> {
            keyboardRow.add(getButton(command, command));
        });

        String messageText = "Я есть магазин";

        var keyBoard = InlineKeyboardMarkup.builder()
                .keyboardRow(keyboardRow)
                .build();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
        AnswerCallbackQuery close = AnswerCallbackQuery.builder()
                .callbackQueryId(update.getCallbackQuery().getId())
                .build();
        EditMessageText message = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(messageText)
                .build();
        EditMessageReplyMarkup replyMarkup = EditMessageReplyMarkup.builder()
                .chatId(chatId)
                .messageId(messageId)
                .replyMarkup(keyBoard)
                .build();
        closeQueryAndRewriteMessage(close, message, replyMarkup);
    }

    @Override
    public void drawIndexForm(IndexForm form) {
        String chatId;
        switch (TelegramUtils.extractTelegramUserId(update)) {
            case ResultOk<Long, Error> ok -> chatId = ok.unwrap().toString();
            case ResultError<Long, Error> err -> {
                draw(err.err());
                return;
            }
        }
        InlineKeyboardRow keyboardRow = new InlineKeyboardRow();
        form.getCommands().forEach(command -> {
            keyboardRow.add(getButton(command, command));
        });

        String messageText = "Привет. Чего желаете";

        var keyBoard = InlineKeyboardMarkup.builder()
                .keyboardRow(keyboardRow)
                .build();
        if (update.hasMessage() && update.getMessage().getText().equals("/start")) {
            SendMessage message = SendMessage.builder()
                    .chatId(chatId)
                    .text(messageText)
                    .replyMarkup(keyBoard)
                    .build();
            sendMessage(message);
            return;
        }

        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
        AnswerCallbackQuery close = AnswerCallbackQuery.builder()
                .callbackQueryId(update.getCallbackQuery().getId())
                .build();
        EditMessageText message = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(messageText)
                .build();
        EditMessageReplyMarkup replyMarkup = EditMessageReplyMarkup.builder()
                .chatId(chatId)
                .messageId(messageId)
                .replyMarkup(keyBoard)
                .build();
        closeQueryAndRewriteMessage(close, message, replyMarkup);
    }

    @Override
    public void drawCatalogForm(CatalogForm form) {
        String chatId;
        switch (TelegramUtils.extractTelegramUserId(update)) {
            case ResultOk<Long, Error> ok -> chatId = ok.unwrap().toString();
            case ResultError<Long, Error> err -> {
                draw(err.err());
                return;
            }
        }
        List<InlineKeyboardRow> keyboardRowList = new ArrayList<>();
        form.getCommands().forEach(command -> {
            InlineKeyboardRow keyboardRow = new InlineKeyboardRow();
            String buttonText = command.contains(ProductAboutCmd.NAME) ? command.split("\\?")[1] : command;
            keyboardRow.add(getButton(buttonText, command));
            keyboardRowList.add(keyboardRow);
        });

        String messageText = "Каталог";

        var keyBoard = InlineKeyboardMarkup.builder()
                .keyboard(keyboardRowList)
                .build();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
        AnswerCallbackQuery close = AnswerCallbackQuery.builder()
                .callbackQueryId(update.getCallbackQuery().getId())
                .build();
        EditMessageText message = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(messageText)
                .build();
        EditMessageReplyMarkup replyMarkup = EditMessageReplyMarkup.builder()
                .chatId(chatId)
                .messageId(messageId)
                .replyMarkup(keyBoard)
                .build();
        closeQueryAndRewriteMessage(close, message, replyMarkup);
    }

    @Override
    public void drawProductAboutForm(ProductAboutForm form) {
        String chatId;
        switch (TelegramUtils.extractTelegramUserId(update)) {
            case ResultOk<Long, Error> ok -> chatId = ok.unwrap().toString();
            case ResultError<Long, Error> err -> {
                draw(err.err());
                return;
            }
        }
        Product product = form.product();
        List<InlineKeyboardRow> keyboardRowList = new ArrayList<>();
        form.getCommands().forEach(command -> {
            InlineKeyboardRow keyboardRow = new InlineKeyboardRow();
            String buttonText = command.contains(ProductAboutCmd.NAME) ? command.split("\\?")[1] : command;
            keyboardRow.add(getButton(buttonText, command));
            keyboardRowList.add(keyboardRow);
        });
        var button = getButton("В корзине: " + form.quantity(), "not-supported");
        keyboardRowList.add(new InlineKeyboardRow(List.of(button)));

        String messageText = product.getName() + "\n" + product.getCost() + "\n" + product.getDescription();

        var keyBoard = InlineKeyboardMarkup.builder()
                .keyboard(keyboardRowList)
                .build();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
        AnswerCallbackQuery close = AnswerCallbackQuery.builder()
                .callbackQueryId(update.getCallbackQuery().getId())
                .build();
        EditMessageText message = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(messageText)
                .build();
        EditMessageReplyMarkup replyMarkup = EditMessageReplyMarkup.builder()
                .chatId(chatId)
                .messageId(messageId)
                .replyMarkup(keyBoard)
                .build();
        closeQueryAndRewriteMessage(close, message, replyMarkup);
    }

    @Override
    public void draw(Error error) {
        // todo implement this
        log.error("=== error inside the app: {}", error.toString());
    }

    private InlineKeyboardButton getButton(String text, String command) {
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(command)
                .build();
    }

    private void sendMessage(SendMessage message) {
        try {
            log.trace("=== try to send message ===");
            Message res = client.execute(message);
            log.trace("=== send message: {} ===", res);
        } catch (TelegramApiException e) {
            log.error("Telegram error during sending message: ", e);
        }
    }

    private void rewriteMessage(EditMessageText messageText, EditMessageReplyMarkup messageReplyMarkup) {
        try {
            log.trace("Try to rewrite telegram message");
            client.execute(messageText);
            client.execute(messageReplyMarkup);
        } catch (TelegramApiException e) {
            log.error("Telegram error during rewriting message: ", e);
        }
    }

    private void closeQueryAndRewriteMessage(AnswerCallbackQuery answerCallbackQuery,
                                             EditMessageText messageText,
                                             EditMessageReplyMarkup messageReplyMarkup) {
//        try {
//            log.trace("Try to close telegram query and rewrite message");
//            client.execute(answerCallbackQuery);
            rewriteMessage(messageText, messageReplyMarkup);
//        } catch (TelegramApiException e) {
//            log.error("Telegram error during closing query: ", e);
//        }
    }
}
