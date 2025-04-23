package ru.aquamarina.fsm.state;

import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.model.command.Command;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.util.Result;

public class Init implements FsmState {

    public static final String NAME = "Init";

    @Override
    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
        return Result.ok(new Index());
    }

    /**
     * method of this instance should never be invoked
     *
     * @return
     * @throws UnsupportedOperationException in any call
     */
    @Override
    public Form getForm(FsmContextHolder context) {
        // throw an Exception to avoid null return
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return NAME;
    }
}
