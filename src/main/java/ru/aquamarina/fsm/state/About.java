package ru.aquamarina.fsm.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.fsm.form.AboutForm;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.model.command.Command;
import ru.aquamarina.model.command.Start;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotSupportedCommand;
import ru.aquamarina.util.Result;

public class About implements FsmState {

    public static final String NAME = "About";

    private final Logger log = LoggerFactory.getLogger(Index.class);

    @Override
    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
        return switch (command) {
            case ru.aquamarina.model.command.Index index -> Result.ok(new Index());
            case Start start-> Result.ok(new ru.aquamarina.fsm.state.Index());
            default -> Result.error(new NotSupportedCommand());
        };
    }

    @Override
    public Form getForm(FsmContextHolder context) {
        return new AboutForm();
    }

    @Override
    public String toString() {
        return NAME;
    }
}
