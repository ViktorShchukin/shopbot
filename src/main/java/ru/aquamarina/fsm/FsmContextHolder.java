package ru.aquamarina.fsm;

import jakarta.inject.Singleton;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import ru.aquamarina.service.*;

@Singleton
public class FsmContextHolder {

    private final ProductService productService;
    private final OkHttpTelegramClient telegramClient;
    private final TelegramInfoService telegramInfoService;
    private final BasketService basketService;
    private final OrderService orderService;
    private final TelegramService telegramService;

    public FsmContextHolder(ProductService productService, OkHttpTelegramClient okHttpTelegramClient, TelegramInfoService getUserTelegramInfoService, BasketService basketService, OrderService orderService, TelegramService telegramService) {
        this.productService = productService;
        this.telegramClient = okHttpTelegramClient;
        this.telegramInfoService = getUserTelegramInfoService;
        this.basketService = basketService;
        this.orderService = orderService;
        this.telegramService = telegramService;
    }

    public OkHttpTelegramClient getTelegramClient() {
        return telegramClient;
    }

    public ProductService getProductService() {
        return productService;
    }

    public TelegramInfoService getTelegramInfoService() {
        return telegramInfoService;
    }

    public BasketService getBasketService() {
        return basketService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public TelegramService getTelegramService() {
        return telegramService;
    }
}
