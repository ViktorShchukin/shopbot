package ru.aquamarina.fsm.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.fsm.form.IndexForm;
import ru.aquamarina.model.command.*;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotSupportedCommand;
import ru.aquamarina.util.Result;

public class IndexState implements FsmState {

    public static final String NAME = "Start";

    private final Logger log = LoggerFactory.getLogger(IndexState.class);

    private final User user;

    public IndexState(User user) {
        this.user = user;
    }

    @Override
    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
        return switch (command) {
            case AboutCmd ndx -> Result.ok(new AboutState(user));
            case ForWholesalerCmd wls -> Result.ok(new ForWholesalerState(user));
            case PayAndDeliveryCmd pad -> Result.ok(new PayAndDeliveryState(user));
            case CatalogCmd ctg -> Result.ok(new CatalogState(user, "/"));
            case StartCmd start-> Result.ok(new IndexState(user));
            default -> Result.error(new NotSupportedCommand());
        };
    }

    @Override
    public Form getForm(FsmContextHolder context) {
        return new IndexForm();
    }

    @Override
    public String toString() {
        return NAME;
    }
}
