package ru.aquamarina.fsm;

import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.model.command.Command;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.util.Result;

public interface FsmRunner {

    Result<Form, Error> execute(Command command);
}
