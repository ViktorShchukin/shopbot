package ru.aquamarina.fsm;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.aquamarina.fsm.state.*;

import java.util.Optional;

public class Resolve implements FsmState {

    public static Optional<FsmState> getInstance() {
        return Optional.of(new Resolve());
    }

    @Override
    public Optional<FsmState> doWork(FsmContextHolder context, Update update) {
        if (update.hasMessage()) {
            if (update.getMessage().getText().equals("/start")) {
                return Optional.of(new Init());
            } else {
                return Optional.of(new UnknownCommandState());
            }
        } else if (update.hasCallbackQuery()) {
            switch (update.getCallbackQuery().getData()) {
                case "index" -> {
                    return Optional.of(new Start());
                }
                case "about" -> {
                    return Optional.of(new About());
                }
                case "catalog" -> {
                    return Optional.of(new Products());
                }
                case String s when s.contains("product") && s.contains("about") -> {
                    return Optional.of(new ProductAbout());
                }
                default -> {
                    return Optional.of(new UnknownCommandState());
                }
            }
        } else {
            return Optional.of(new UnknownCommandState());
        }
    }

    @Override
    public Form getForm() {
        // todo
        return null;
    }
}
