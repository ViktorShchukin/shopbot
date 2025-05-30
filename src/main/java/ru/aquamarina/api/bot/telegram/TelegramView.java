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
import ru.aquamarina.model.command.*;
import ru.aquamarina.model.entity.Folder;
import ru.aquamarina.model.entity.Product;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.service.ProductService;
import ru.aquamarina.util.PathUtil;
import ru.aquamarina.util.ResultError;
import ru.aquamarina.util.ResultOk;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record TelegramView(OkHttpTelegramClient client, Update update, ProductService productService) implements View {

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
        InlineKeyboardRow keyboardRow = new InlineKeyboardRow(
                getButton(new IndexCmd(null))
        );

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
        InlineKeyboardRow keyboardRow = new InlineKeyboardRow(
                getButton(new AboutCmd(null)),
                getButton(new CatalogCmd(null))
        );

        InlineKeyboardRow keyboardRow1 = new InlineKeyboardRow(
                getButton(new ForWholesalerCmd(null)),
                getButton(new PayAndDeliveryCmd(null))
        );

        String messageText = "Привет. Чего желаете";

        var keyBoard = InlineKeyboardMarkup.builder()
                .keyboard(List.of(keyboardRow, keyboardRow1))
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
        List<Command> productInFolder = form.products().stream()
                .map(product -> new ProductAboutCmd(null, product.getId()))
                .collect(Collectors.toList());
        List<Command> folderInFolder = form.folders().stream()
                .map(Folder::path)
                .map(pth -> new FolderCmd(null, pth))
                .collect(Collectors.toList());
        List<Command> commands;
        if (form.path().equals("/")) {
            commands = List.of(
                    new BasketCmd(null),
                    new IndexCmd(null)
            );
        } else {
            commands = List.of(
                    new BasketCmd(null),
                    new CatalogCmd(null),
                    new IndexCmd(null)
            );
        }

        List<InlineKeyboardRow> keyboardRowList = new ArrayList<>();
        Stream.of(productInFolder, folderInFolder, commands)
                .flatMap(Collection::stream)
                .forEach(command -> {
                    InlineKeyboardRow keyboardRow = new InlineKeyboardRow();
                    keyboardRow.add(getButton(command));
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
        keyboardRowList.add(new InlineKeyboardRow(
                getButton(new QuantityMinusCmd(null)),
                getButton(new QuantityPlusCmd(null))
        ));
        keyboardRowList.add(new InlineKeyboardRow(
                getButton(new BasketCmd(null)),
                getButton(new InstructionCmd(null))
        ));
        keyboardRowList.add(new InlineKeyboardRow(
                getButton(new CatalogCmd(null))
        ));
        keyboardRowList.add(new InlineKeyboardRow(
                getButton(new IndexCmd(null))
        ));

        String messageText = product.getName() + "\n" + "Цена: " + (double) product.getCost() / 100 + " руб" + "\n" + "В корзине: " + form.quantity();

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
    public void drawOrderForm(OrderForm form) {
        String chatId;
        switch (TelegramUtils.extractTelegramUserId(update)) {
            case ResultOk<Long, Error> ok -> chatId = ok.unwrap().toString();
            case ResultError<Long, Error> err -> {
                draw(err.err());
                return;
            }
        }
        List<InlineKeyboardRow> keyboardRowList = new ArrayList<>();
        InlineKeyboardRow keyboardRow = new InlineKeyboardRow(
                getButton(new IndexCmd(null))
        );
        keyboardRowList.add(keyboardRow);

        // todo get rid of Optional.get() call without check
        String products = form.rows().stream()
                .map(basketRow -> productService.getById(basketRow.getProductId()).ok().get().getName() + "  " + basketRow.getQuantity().toString() + "\n")
                .reduce("", String::concat);
        String messageText = products + "Сумма: " + (double) form.totalCost() / 100 + "\nСпасибо за заказ.\nМы свяжемся с вами позже.";

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
    public void drawBasketForm(BasketForm form) {
        String chatId;
        switch (TelegramUtils.extractTelegramUserId(update)) {
            case ResultOk<Long, Error> ok -> chatId = ok.unwrap().toString();
            case ResultError<Long, Error> err -> {
                draw(err.err());
                return;
            }
        }
        List<InlineKeyboardRow> keyboardRowList = new ArrayList<>();
        InlineKeyboardRow keyboardRow = new InlineKeyboardRow(
                getButton(new DoOrderCmd(null)),
                getButton(new CatalogCmd(null))
        );
        keyboardRowList.add(keyboardRow);

        // todo get rid of Optional.get() call without check
        String products = form.rows().stream()
                .map(basketRow -> productService.getById(basketRow.getProductId()).ok().get().getName() + "  " + basketRow.getQuantity().toString() + "\n")
                .reduce("", String::concat);
        String messageText = "Корзина" + "\n" + products + "Сумма: " + (double) form.totalCost() / 100;

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
    public void drawForWholesalerForm(ForWholesalerForm forWholesalerForm) {
        String chatId;
        switch (TelegramUtils.extractTelegramUserId(update)) {
            case ResultOk<Long, Error> ok -> chatId = ok.unwrap().toString();
            case ResultError<Long, Error> err -> {
                draw(err.err());
                return;
            }
        }
        InlineKeyboardRow keyboardRow = new InlineKeyboardRow(
                getButton(new IndexCmd(null))
        );

        String messageText = "Оптовикам";

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
    public void drawPayAndDeliveryFormForm(PayAndDeliveryForm payAndDeliveryForm) {
        String chatId;
        switch (TelegramUtils.extractTelegramUserId(update)) {
            case ResultOk<Long, Error> ok -> chatId = ok.unwrap().toString();
            case ResultError<Long, Error> err -> {
                draw(err.err());
                return;
            }
        }
        InlineKeyboardRow keyboardRow = new InlineKeyboardRow(
                getButton(new IndexCmd(null))
        );

        String messageText = "Оплата и доставка";

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

    private InlineKeyboardButton getButton(Command command) {
        String text = switch (command) {
            case AboutCmd cmd -> "О нас";
            case ForWholesalerCmd cmd -> "Оптовикам";
            case PayAndDeliveryCmd cmd -> "Оплата и доставка";
            case AddToBasketCmd cmd -> "Добавить в корзину";
            case BasketCmd cmd -> "Посмотреть корзину";
            case CatalogCmd cmd -> "Каталог товаров";
            case DoOrderCmd cmd -> "Оформить заказ";
            case FolderCmd cmd -> "Папка " + PathUtil.getFolderName(cmd.path());
            case IndexCmd cmd -> "На главную";
            case InstructionCmd cmd -> "Инструкция к товару";
            case ProductAboutCmd cmd -> productService.getById(cmd.productId()).ok().get().getName();
            case QuantityMinusCmd cmd -> "Убрать из корзины";
            case QuantityPlusCmd cmd -> "Добавить в корзину";
            case StartCmd cmd -> "Restart session. This command should not appear in user interface.";
        };
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(command.toString())
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

    private void closeQuery(AnswerCallbackQuery answerCallbackQuery) {
        try {
            log.trace("Try to close telegram query");
            client.execute(answerCallbackQuery);
        } catch (TelegramApiException e) {
            log.error("Telegram error during closing query: ", e);
        }
    }

    private void closeQueryAndRewriteMessage(AnswerCallbackQuery answerCallbackQuery,
                                             EditMessageText messageText,
                                             EditMessageReplyMarkup messageReplyMarkup) {
        closeQuery(answerCallbackQuery);
        rewriteMessage(messageText, messageReplyMarkup);

    }
}
