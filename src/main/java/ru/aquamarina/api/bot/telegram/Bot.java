package ru.aquamarina.api.bot.telegram;

import io.micronaut.context.annotation.Property;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.aquamarina.fsm.FsmRunner;
import ru.aquamarina.fsm.form.ErrorForm;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.fsm.state.ErrorState;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.UnknownCommand;
import ru.aquamarina.service.UserService;
import ru.aquamarina.util.Result;
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
    private final TelegramUtils telegramUtils;
    private final TelegramView view;
    private final UserService userService;

    public Bot(TelegramMapper telegramMapper, FsmRunner fsmRunner, TelegramUtils telegramUtils, TelegramView view, UserService userService) {
        this.telegramMapper = telegramMapper;
        this.fsmRunner = fsmRunner;
        this.telegramUtils = telegramUtils;
        this.view = view;
        this.userService = userService;
    }

    @Override
    public void consume(Update update) {
        try {
            evaluateUpdate(update);
        } catch (Exception e) {
            log.error("Error during telegram update process", e);
        } catch (Throwable e) {
            log.error("Fatal error during telegram update process", e);
            throw e;
        }
    }

    @Override
    public void consume(List<Update> updates) {
        updates.forEach(this::consume);
    }

    private void evaluateUpdate(Update update) {

        var res = telegramUtils.getUser(update)
                .map(user -> telegramMapper.mapToCommand(update, user))
                .map(fsmRunner::execute);

        switch (res) {
            case ResultOk<Form, Error> ok -> ok.unwrap().draw(view);
            case ResultError<Form, Error> err -> {
                var error = err.err();
                if (error instanceof UnknownCommand unknownCommand) {
                    var form = new ErrorForm(unknownCommand.user());
                    view.drawErrorForm(form);
                    userService.updateState(unknownCommand.user(), new ErrorState(unknownCommand.user()));
                    return;
                }
                view.draw(error);
            }
        }
    }

    public String getBotToken() {
        return botToken;
    }
}
