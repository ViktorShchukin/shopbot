package ru.aquamarina.service;

import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.context.event.StartupEvent;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Singleton
public class StartUpEventListener implements ApplicationEventListener<StartupEvent> {

    private final Logger log = LoggerFactory.getLogger(StartUpEventListener.class);

    private final TelegramBotsLongPollingApplication telegramApp;
    private final Bot bot;

    public StartUpEventListener(TelegramBotsLongPollingApplication telegramApp, Bot bot) {
        this.telegramApp = telegramApp;
        this.bot = bot;
    }

    @Override
    public void onApplicationEvent(StartupEvent event) {
        try {
            telegramApp.registerBot(bot.botToken, bot);
        } catch (TelegramApiException e) {
         log.error("Error registering bot", e);
        }
    }
}
