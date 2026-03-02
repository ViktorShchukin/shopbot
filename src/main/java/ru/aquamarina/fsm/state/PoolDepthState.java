package ru.aquamarina.fsm.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.fsm.form.PoolDepthForm;
import ru.aquamarina.model.command.Command;
import ru.aquamarina.model.command.StartCmd;
import ru.aquamarina.model.command.UserInputCmd;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotSupportedCommand;
import ru.aquamarina.util.Result;

public class PoolDepthState implements FsmState {

    public static final String NAME = "PoolDepth";

    private final Logger log = LoggerFactory.getLogger(IndexState.class);

    private final User user;

    public PoolDepthState(User user) {
        this.user = user;
    }

    @Override
    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
        return switch (command) {
            case UserInputCmd input -> context.getPoolInfoService()
                    .update(user.getId(), 1L, null, null, null, null)
                    .mapValue(res -> new PoolDiameterState(user));
            case StartCmd start -> Result.ok(new IndexState(user, true));
            default -> Result.error(new NotSupportedCommand());
        };
    }

    @Override
    public Form getForm(FsmContextHolder context) {
        return new PoolDepthForm(user);
    }

    @Override
    public String toString() {
        return NAME;
    }
}
