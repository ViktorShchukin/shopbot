package ru.aquamarina.service;

import jakarta.inject.Singleton;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.aquamarina.fsm.FsmState;
import ru.aquamarina.fsm.StateResolver;

import java.util.Optional;

@Singleton
public class Bot extends TelegramLongPollingBot {

    private final Logger log = LoggerFactory.getLogger(Bot.class);

    private final StateResolver stateResolver;

    public Bot(StateResolver stateResolver){
        super();
        this.stateResolver = stateResolver;
    }

    @Override
    public String getBotUsername() {
        return "shopbot";
    }

    @Override
    public String getBotToken() {
        return "7920461898:AAFOOcHsJYIV0UTIdbAuwqcH8C_XDeXuDqw";
    }

    @Override
    public void onUpdateReceived(Update update) {
        FsmState state = stateResolver.resolve(update, this);
        var st = Optional.ofNullable(state);
        while (st.isPresent()) {
            st = st.get().doWork(update);
        }
//        state.doWork(update);
    }
}
