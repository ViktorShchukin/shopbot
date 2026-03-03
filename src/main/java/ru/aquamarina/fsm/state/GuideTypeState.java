package ru.aquamarina.fsm.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.fsm.form.GuideTypeForm;
import ru.aquamarina.model.command.Command;
import ru.aquamarina.model.command.GuideTypeCommand;
import ru.aquamarina.model.command.StartCmd;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotSupportedCommand;
import ru.aquamarina.util.Result;

public class GuideTypeState implements FsmState {
    public static final String NAME = "GuideType";

    private final Logger log = LoggerFactory.getLogger(IndexState.class);

    private final User user;

    public GuideTypeState(User user) {
        this.user = user;
    }

    @Override
    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
        return switch (command) {
            case GuideTypeCommand gtp -> Result.ok(new GuideState(user, gtp.guideType()));
            case StartCmd start -> Result.ok(new IndexState(user, true));
            default -> Result.error(new NotSupportedCommand());
        };
    }

    @Override
    public Form getForm(FsmContextHolder context) {
        return new GuideTypeForm(user);
    }

    @Override
    public String toString() {
        return NAME;
    }
}
