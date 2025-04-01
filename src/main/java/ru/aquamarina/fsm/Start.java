package ru.aquamarina.fsm;

import java.util.Optional;

public class Start implements FsmState{

    @Override
    public Optional<FsmState> doWork() {
        return Optional.empty();
    }
}
