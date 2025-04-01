package ru.aquamarina.service;

import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
//import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

@Factory
public class TelegramAppFactory {

    @Singleton
    TelegramBotsApi telApp() {
        TelegramBotsApi api = null;
        try {
            api = new TelegramBotsApi(DefaultBotSession.class);
        } catch (TelegramApiException e) {
            System.out.println("как же бесть это все");
        }
        return api;
    }
}
