package ru.aquamarina.api.bot.telegram;

import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Property;
import jakarta.inject.Singleton;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import ru.aquamarina.api.bot.View;


@Factory
public class TelegramAppFactory {

    @Property(name = "sb.chatbot.telegram.bot.token")
    protected String botToken;

    @Singleton
    TelegramMapper getTelegramMapper(){
        return TelegramMapper.getInstance();
    }

    @Singleton
    OkHttpTelegramClient getTelegramClient() {
        return new OkHttpTelegramClient(botToken);
    }

    @Singleton
    TelegramBotsLongPollingApplication telApp() {
        return new TelegramBotsLongPollingApplication();
    }

    @Singleton
    View<TelegramDrawContext> getTelegramView(OkHttpTelegramClient client){
        return new TelegramView(client);
    }
}
