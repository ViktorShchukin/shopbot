package ru.aquamarina.fsm.state;

import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.fsm.form.OrderForm;
import ru.aquamarina.model.command.*;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotSupportedCommand;
import ru.aquamarina.util.Result;

public class OrderState implements FsmState {

    public final static String NAME = "Order";

    @Override
    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
        return switch (command) {
            case Start start -> Result.ok(new ru.aquamarina.fsm.state.Index());
            case ru.aquamarina.model.command.Index ndx -> Result.ok(new ru.aquamarina.fsm.state.Index());
            default -> Result.error(new NotSupportedCommand());
        };
    }

    @Override
    public Form getForm(FsmContextHolder context) {
        return new OrderForm();
    }

    @Override
    public String toString() {
        return NAME;
    }
}
