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
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.util.ResultError;
import ru.aquamarina.util.ResultOk;

import java.util.List;

@Singleton
public class Bot implements LongPollingSingleThreadUpdateConsumer {

    private final Logger log = LoggerFactory.getLogger(Bot.class);
    @Property(name = "sb.chatbot.telegram.bot.token")
    protected String botToken;
    private final TelegramMapper telegramMapper;
    private final FsmRunner fsmRunner;
    private final View<TelegramDrawContext> view;
    private final TelegramUtils telegramUtils;

    public Bot(TelegramMapper telegramMapper, FsmRunner fsmRunner, View view, TelegramUtils telegramUtils) {
        this.telegramMapper = telegramMapper;
        this.fsmRunner = fsmRunner;
        this.view = view;
        this.telegramUtils = telegramUtils;
    }

    @Override
    public void consume(Update update) {
        User user;
        // todo redo this code with normal chaining
        switch (telegramUtils.getUser(update)) {
            case ResultOk<User, Error> ok -> {user = ok.result();log.info("=== got user ===");}
            case ResultError err -> {log.error("=== get user err {}===", err);return;}
        }
        TelegramDrawContext drawContext = telegramMapper.mapToDrawContext(update).ok().get();
        var res = telegramMapper
                .mapToCommand(update, user)
                .map(fsmRunner::execute);
//                .map(form -> view.draw(drawContext, form))
//                .or(error -> view.drawError(drawContext, error));

        switch (res) {
            case ResultOk<Form, Error> ok -> view.draw(drawContext, ok.unwrap());
            case ResultError<Form, Error> err -> view.drawError(drawContext, err.err());
        }
    }

    @Override
    public void consume(List<Update> updates) {
        updates.forEach(this::consume);
    }

    public String getBotToken() {
        return botToken;
    }
}
