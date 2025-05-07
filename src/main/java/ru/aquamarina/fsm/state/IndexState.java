package ru.aquamarina.fsm.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.fsm.form.IndexForm;
import ru.aquamarina.model.command.AboutCmd;
import ru.aquamarina.model.command.CatalogCmd;
import ru.aquamarina.model.command.Command;
import ru.aquamarina.model.command.StartCmd;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotSupportedCommand;
import ru.aquamarina.util.Result;

public class IndexState implements FsmState {

    public static final String NAME = "Start";

    private final Logger log = LoggerFactory.getLogger(IndexState.class);

    @Override
    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
        return switch (command) {
            case AboutCmd ndx -> Result.ok(new AboutState());
            case CatalogCmd ctg -> Result.ok(new CatalogState("/"));
            case StartCmd start-> Result.ok(new IndexState());
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
