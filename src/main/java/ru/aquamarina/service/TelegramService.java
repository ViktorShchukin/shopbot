package ru.aquamarina.service;

import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.aquamarina.model.UserRole;
import ru.aquamarina.model.entity.Order;
import ru.aquamarina.model.entity.TelegramInfo;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.util.Result;

import java.util.List;

@Singleton
public class TelegramService {

    private static final Logger log = LoggerFactory.getLogger(TelegramService.class);

    private final OkHttpTelegramClient telegramClient;
    private final UserService userService;
    private final TelegramInfoService telegramInfoService;
    private final OrderService orderService;
    private final ProductService productService;

    public TelegramService(OkHttpTelegramClient telegramClient, UserService userService, TelegramInfoService telegramInfoService, OrderService orderService, ProductService productService) {
        this.telegramClient = telegramClient;
        this.userService = userService;
        this.telegramInfoService = telegramInfoService;
        this.orderService = orderService;
        this.productService = productService;
    }

    public void notifySeller(Order order) {
        String messageText = orderService.getOrderRow(order).stream()
                // todo get rid of call get without check
                .map(orderRow -> "товар: " + productService.getById(orderRow.getProductId()).ok().get().getName() + " кол-во:" + orderRow.getQuantity().toString())
                .reduce("", (acc, element) -> acc + "\n" + element);

        String clientId = userService.getUser(order.getUserId())
                .map(telegramInfoService::getByUser)
                .flatMap(res -> res.ok())
                .map(TelegramInfo::getTelegramId)
                .map(String::valueOf)
                // todo get rid of get() call
                .get();
        String clientUserName = userService.getUser(order.getUserId())
                .map(telegramInfoService::getByUser)
                .flatMap(res -> res.ok())
                .map(TelegramInfo::getUserName)
                .get();

        messageText = messageText + "\n" + "[@%s](tg://user?id=%s)".formatted(clientUserName, clientId);

        SendMessage.SendMessageBuilder messageBuilder = SendMessage.builder()
                .text(messageText)
                .parseMode("Markdown");

        List<SendMessage> messages = telegramInfoService.getByUserRole(UserRole.SELLER).stream()
                .map(TelegramInfo::getTelegramId)
                .map(chatId -> messageBuilder.chatId(chatId))
                .map(SendMessage.SendMessageBuilder::build)
                .toList();

        if (messages.isEmpty()) {
            log.error("Can not notify any user about order");
        }

        // todo maybe return result instead of void???
        messages.forEach(this::sendMessage);
    }

    private void sendMessage(SendMessage message) {
        try {
            log.trace("=== try to send message ===");
            Message res = telegramClient.execute(message);
            log.trace("=== send message: {} ===", res);
        } catch (TelegramApiException e) {
            log.error("Telegram error during sending message: ", e);
        }
    }
}
