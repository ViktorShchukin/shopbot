package ru.aquamarina.fsm.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.fsm.form.IndexForm;
import ru.aquamarina.model.command.Command;
import ru.aquamarina.model.command.Start;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotSupportedCommand;
import ru.aquamarina.util.Result;

public class Index implements FsmState {

    public static final String NAME = "Start";

    private final Logger log = LoggerFactory.getLogger(Index.class);

    @Override
    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
        return switch (command) {
            case ru.aquamarina.model.command.About ndx -> Result.ok(new About());
            case ru.aquamarina.model.command.Catalog ctg -> Result.ok(new Catalog());
            case Start start-> Result.ok(new ru.aquamarina.fsm.state.Index());
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
