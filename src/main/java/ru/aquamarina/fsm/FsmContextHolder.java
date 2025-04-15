package ru.aquamarina.fsm;

import jakarta.inject.Singleton;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import ru.aquamarina.service.ProductService;
import ru.aquamarina.service.TelegramInfoService;

@Singleton
public class FsmContextHolder {

    private final ProductService productService;
    private final OkHttpTelegramClient telegramClient;
    private final TelegramInfoService telegramInfoService;

    public FsmContextHolder(ProductService productService, OkHttpTelegramClient okHttpTelegramClient, TelegramInfoService getUserTelegramInfoService) {
        this.productService = productService;
        this.telegramClient = okHttpTelegramClient;
        this.telegramInfoService = getUserTelegramInfoService;
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
}
