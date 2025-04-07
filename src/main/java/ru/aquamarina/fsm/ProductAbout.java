package ru.aquamarina.fsm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.aquamarina.model.Product;
import ru.aquamarina.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

public class ProductAbout implements FsmState {

    private final Logger log = LoggerFactory.getLogger(ProductAbout.class);

    private final ProductRepository productRepository;
    private final AbsSender sender;

    public ProductAbout(ProductRepository productRepository, AbsSender sender) {
        this.productRepository = productRepository;
        this.sender = sender;
    }

    @Override
    public Optional<FsmState> doWork(Update update) {
        var chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        String name = extractName(update.getCallbackQuery().getData());
        Product product = productRepository.findByName(name);
        var button = InlineKeyboardButton.builder()
                .text("Инструкция")
                // todo
                .callbackData("todo")
                .build();
        var button1 = InlineKeyboardButton.builder()
                .text("Добавить в корзину")
                // todo
                .callbackData("todo")
                .build();
        var button2 = InlineKeyboardButton.builder()
                .text("Назад")
                .callbackData("catalog")
                .build();
        AnswerCallbackQuery close = AnswerCallbackQuery.builder()
                .callbackQueryId(update.getCallbackQuery().getId())
                .build();
        var keyBoard = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(button, button1, button2))
                .build();
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(product.getName() + "\nОписание: " + product.getDescription() + "\nЦена: " + product.getCost().toString())
                .replyMarkup(keyBoard)
                .build();

        try {
            sender.execute(close);
            sender.execute(message);
        } catch (TelegramApiException e) {
            log.error("some err", e);
        }
        return Optional.empty();
    }

    String extractName(String query) {
        return query.split("/")[1];
    }
}
