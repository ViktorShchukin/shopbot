package ru.aquamarina.fsm;

import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.fsm.state.FsmState;
import ru.aquamarina.fsm.state.Init;
import ru.aquamarina.fsm.state.Start;
import ru.aquamarina.model.command.Command;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.UnknownState;
import ru.aquamarina.service.UserService;
import ru.aquamarina.util.Result;

@Singleton
public class DefaultFsmRunner implements FsmRunner {

    private static final Logger log = LoggerFactory.getLogger(DefaultFsmRunner.class);

    private final UserService userService;
    private final FsmContextHolder fsmContextHolder;

    public DefaultFsmRunner(UserService userService, FsmContextHolder fsmContextHolder) {
        this.userService = userService;
        this.fsmContextHolder = fsmContextHolder;
    }

    @Override
    public Result<Form, Error> execute(Command command) {
        return restoreState(command.getUser())
                .map(state -> state.doWork(fsmContextHolder, command))
                .map(state -> Result.ok(state.getForm()));
    }

    private Result<FsmState, Error> restoreState(User user) {
        String caseName = user.getLastState();
        return switch (caseName) {
            case Start.NAME -> Result.ok(new Start());
            case null -> Result.ok(new Init());
            default -> Result.error(new UnknownState());
        };
    }
}
