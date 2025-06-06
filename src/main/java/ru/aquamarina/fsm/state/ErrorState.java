package ru.aquamarina.fsm.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.fsm.form.ErrorForm;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.model.command.Command;
import ru.aquamarina.model.command.IndexCmd;
import ru.aquamarina.model.command.StartCmd;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotSupportedCommand;
import ru.aquamarina.util.Result;

import java.util.Optional;

public class ErrorState implements FsmState {

    public static final String NAME = "Error";

    private final Logger log = LoggerFactory.getLogger(CatalogState.class);

    private final User user;
    private final Optional<Error> error;

    public ErrorState(User user, Error error) {
        this.user = user;
        this.error = Optional.of(error);
    }

    public ErrorState(User user) {
        this.user = user;
        this.error = Optional.empty();
    }

    @Override
    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
        return switch (command) {
            case IndexCmd ndx -> Result.ok(new IndexState(user));
            case StartCmd start -> Result.ok(new IndexState(user));
            default -> Result.error(new NotSupportedCommand());
        };
    }

    @Override
    public Form getForm(FsmContextHolder context) {
        return new ErrorForm();
    }

    @Override
    public String toString() {
        return NAME;
    }
}
