package ru.aquamarina.service;

import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.lang.annotation.Annotation;

@Singleton
public class StartUpEventListener implements ApplicationEventListener<StartupEvent> {

    private final Logger log = LoggerFactory.getLogger(StartUpEventListener.class);

    private final TelegramBotsApi telegramApp;
    private final Bot bot;

    public StartUpEventListener(TelegramBotsApi telegramApp, Bot bot) {
        this.telegramApp = telegramApp;
        this.bot = bot;
    }

    @Override
    public void onApplicationEvent(StartupEvent event) {
        try {
            telegramApp.registerBot(bot);
        } catch (TelegramApiException e) {
         log.error("Error registering bot", e);
        }
    }
}
