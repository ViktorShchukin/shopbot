package ru.aquamarina.api.bot.telegram;

import io.micronaut.context.annotation.Property;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.aquamarina.api.bot.View;
import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.fsm.FsmRunner;
import ru.aquamarina.model.entity.User;

import java.util.List;

@Singleton
public class Bot implements LongPollingSingleThreadUpdateConsumer {

    private final Logger log = LoggerFactory.getLogger(Bot.class);
    @Property(name = "sb.chatbot.telegram.bot.token")
    protected String botToken;
    private final FsmContextHolder fsmContextHolder;
    private final TelegramMapper telegramMapper;
    private final FsmRunner fsmRunner;
    private final View view;
    private final TelegramUtils telegramUtils;

    public Bot(FsmContextHolder fsmContextHolder, TelegramMapper telegramMapper, FsmRunner fsmRunner, View view, TelegramUtils telegramUtils){
        this.fsmContextHolder = fsmContextHolder;
        this.telegramMapper = telegramMapper;
        this.fsmRunner = fsmRunner;
        this.view = view;
        this.telegramUtils = telegramUtils;
    }

    @Override
    public void consume(Update update) {
        User user = telegramUtils.getUser(update).ok().get();
        telegramMapper
                .map(update, user.getId());
                .map(fsmRunner::execute)
                .map(update, user);
                .map(view::draw)
                .onErr(view::drawErr);
        Result<Form, Err> result = fsmRunner.execute(command);
        view.draw(result);
    }

    @Override
    public void consume(List<Update> updates) {
        updates.forEach(this::consume);
    }

    public String getBotToken() {
        return botToken;
    }
}
