package ru.aquamarina.service;

import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.aquamarina.api.bot.telegram.TelegramUtils;
import ru.aquamarina.api.dto.ProductRowDto;
import ru.aquamarina.api.mapper.ProductMapper;
import ru.aquamarina.model.DistributionMode;
import ru.aquamarina.model.UserRole;
import ru.aquamarina.model.entity.Order;
import ru.aquamarina.model.entity.TelegramInfo;

import java.util.List;
import java.util.Optional;

@Singleton
public class TelegramService {

    private static final Logger log = LoggerFactory.getLogger(TelegramService.class);

    private final OkHttpTelegramClient telegramClient;
    private final UserService userService;
    private final TelegramInfoService telegramInfoService;
    private final OrderService orderService;
    private final ProductService productService;
    private final ProductMapper productMapper;

    public TelegramService(OkHttpTelegramClient telegramClient, UserService userService, TelegramInfoService telegramInfoService, OrderService orderService, ProductService productService, ProductMapper productMapper) {
        this.telegramClient = telegramClient;
        this.userService = userService;
        this.telegramInfoService = telegramInfoService;
        this.orderService = orderService;
        this.productService = productService;
        this.productMapper = productMapper;
    }

    public void notifySeller(Order order) {
        List<ProductRowDto> products = orderService.getOrderRow(order).stream()
                .map(basketRow -> productMapper.mapTo(basketRow, productService::getById))
                // todo how to handle that product exist in basket and doesn't exist in product table???
                .map(res -> res.ok())
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        String productTable = TelegramUtils.getProductTable(products);

        long totalCostInCents = products.stream()
                .mapToLong(productRowDto -> productRowDto.product().getCost() * productRowDto.quantity())
                .sum();
        String totalCost = String.valueOf((double) totalCostInCents / 100);

        String clientId = userService.getUser(order.getUserId())
                .map(telegramInfoService::getByUser)
                .flatMap(res -> res.ok())
                .map(TelegramInfo::getTelegramId)
                .map(String::valueOf)
                // todo get rid of get() call
                .orElseGet(() -> "");


        String clientUserName = userService.getUser(order.getUserId())
                .map(telegramInfoService::getByUser)
                .flatMap(res -> res.ok())
                .map(TelegramInfo::getUserName)
                .orElseGet(() -> "клиент");

        String distributionMode = switch (order.getDistributionMode()) {
            case DistributionMode.DELIVERY -> "Доставка";
            case DistributionMode.SERLF_PICKUP -> "Самовывоз";
        };

        String messageText = productTable + "\n" +
                "Сумма: " + totalCost + "\n" +
                "телефон: %s\n".formatted(order.getPhoneNumber()) +
                "адресс: %s\n".formatted(order.getAddress()) +
                "способ доставки: %s\n".formatted(distributionMode) +
                "<a href=\"tg://user?id=%s\">@%s</a> ".formatted(clientId, clientUserName) +
                "или воспользуйтесь ссылкой https://t.me/%s".formatted(clientUserName);

        SendMessage.SendMessageBuilder messageBuilder = SendMessage.builder()
                .text(messageText)
                .parseMode("HTML");

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
