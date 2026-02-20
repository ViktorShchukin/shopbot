package ru.aquamarina.fsm;

import io.pebbletemplates.pebble.PebbleEngine;
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
    private final PebbleEngine pebbleEngine;
    private final PdfService pdfServie;

    public FsmContextHolder(ProductService productService, OkHttpTelegramClient okHttpTelegramClient, TelegramInfoService getUserTelegramInfoService, BasketService basketService, OrderService orderService, TelegramService telegramService, PebbleEngine pebbleEngine, PdfService pdfServie) {
        this.productService = productService;
        this.telegramClient = okHttpTelegramClient;
        this.telegramInfoService = getUserTelegramInfoService;
        this.basketService = basketService;
        this.orderService = orderService;
        this.telegramService = telegramService;
        this.pebbleEngine = pebbleEngine;
        this.pdfServie = pdfServie;
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

    public PebbleEngine getPebbleEngine() {
        return pebbleEngine;
    }

    public PdfService getPdfService() {
        return pdfServie;
    }
}
