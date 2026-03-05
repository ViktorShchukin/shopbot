package ru.aquamarina.fsm.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.fsm.form.PoolTypeForm;
import ru.aquamarina.model.command.*;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotSupportedCommand;
import ru.aquamarina.util.Result;

public class PoolTypeState implements FsmState {

    public static final String NAME = "PoolType";

    private final Logger log = LoggerFactory.getLogger(IndexState.class);

    private final User user;

    public PoolTypeState(User user) {
        this.user = user;
    }

    @Override
    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
        return switch (command) {
            case CircleCmd crl -> Result.ok(new PoolSizeInfoState(user));
            case RectangleCmd rec -> Result.ok(new PoolSizeInfoState(user));
            case StartCmd start-> Result.ok(new IndexState(user, true));
            default -> Result.error(new NotSupportedCommand());
        };
    }

    @Override
    public Form getForm(FsmContextHolder context) {
        return new PoolTypeForm(user);
    }

    @Override
    public String toString() {
        return NAME;
    }
}
