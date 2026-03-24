package ru.aquamarina.fsm.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.fsm.form.AboutForm;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.model.command.Command;
import ru.aquamarina.model.command.ContactCmd;
import ru.aquamarina.model.command.IndexCmd;
import ru.aquamarina.model.command.StartCmd;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotSupportedCommand;
import ru.aquamarina.util.Result;

public class AboutState implements FsmState {

    public static final String NAME = "About";

    private final Logger log = LoggerFactory.getLogger(IndexState.class);

    private final User user;

    public AboutState(User user) {
        this.user = user;
    }

    @Override
    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
        return switch (command) {
            case ContactCmd cnt -> Result.ok(new ContactState(user));
            case StartCmd start-> Result.ok(new IndexState(user, true));
            default -> Result.error(new NotSupportedCommand());
        };
    }

    @Override
    public Form getForm(FsmContextHolder context) {
        return new AboutForm(user);
    }

    @Override
    public String toString() {
        return NAME;
    }
}
