package ru.aquamarina.fsm;

import ru.aquamarina.model.command.Command;
import ru.aquamarina.util.Result;

public interface FsmRunner {

    Result execute(Command command);
}
