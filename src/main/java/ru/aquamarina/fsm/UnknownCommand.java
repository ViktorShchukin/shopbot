package ru.aquamarina.fsm;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

public class UnknownCommand implements FsmState{
    @Override
    public Optional<FsmState> doWork(Update update) {
        return Optional.empty();
    }
}
