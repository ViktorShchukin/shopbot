package ru.aquamarina.api.bot.telegram;

import io.micronaut.context.annotation.Property;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.fsm.FsmState;
import ru.aquamarina.fsm.Resolve;

import java.util.List;
import java.util.Optional;

@Singleton
public class Bot implements LongPollingSingleThreadUpdateConsumer {

    private final Logger log = LoggerFactory.getLogger(Bot.class);
    @Property(name = "sb.chatbot.telegram.bot.token")
    protected String botToken;
    private final FsmContextHolder fsmContextHolder;

    public Bot(FsmContextHolder fsmContextHolder){
        this.fsmContextHolder = fsmContextHolder;
    }

    @Override
    public void consume(Update update) {
        Optional<FsmState> state = Resolve.getInstance();
        while (state.isPresent()){
            state = state.get().doWork(fsmContextHolder, update);
        }
    }

    @Override
    public void consume(List<Update> updates) {
        updates.forEach(this::consume);
    }

    public String getBotToken() {
        return botToken;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }
}
