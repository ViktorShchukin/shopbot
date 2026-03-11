package ru.aquamarina.fsm.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.fsm.form.FilterTypeForm;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.model.command.*;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotSupportedCommand;
import ru.aquamarina.util.Result;

import java.io.File;

public class FilterTypeState implements FsmState {
    public static final String NAME = "FilterType";

    private final Logger log = LoggerFactory.getLogger(GuideState.class);

    private final User user;

    public FilterTypeState(User user) {
        this.user = user;
    }

    @Override
    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
        return switch (command) {
            case FilterTypeCmd cmd -> context.getPoolInfoService()
                    .updateFilterType(user.getId(), cmd.filterType())
                    .map(res -> Result.ok(new PoolDepthState(user)));
            case StartCmd start -> Result.ok(new IndexState(user, true));
            default -> Result.error(new NotSupportedCommand());
        };
    }

    @Override
    public Form getForm(FsmContextHolder context) {
        return new FilterTypeForm(user);
    }

    @Override
    public String toString() {
        return NAME;
    }
}
