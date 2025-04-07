package ru.aquamarina.fsm;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

public interface FsmState {

    Optional<FsmState> doWork(Update update);
}
