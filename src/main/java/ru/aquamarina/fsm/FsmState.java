package ru.aquamarina.fsm;

import java.util.Optional;

public interface FsmState {

    Optional<FsmState> doWork();
}
