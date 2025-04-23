package ru.aquamarina.fsm.state;

import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.model.command.Command;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.util.Result;

public interface FsmState {

    Result<FsmState, Error> doWork(FsmContextHolder context, Command command);

    Form getForm(FsmContextHolder context);
}
