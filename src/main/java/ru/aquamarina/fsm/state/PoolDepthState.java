package ru.aquamarina.fsm.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.fsm.form.InvalidInputForLongForm;
import ru.aquamarina.fsm.form.PoolDepthForm;
import ru.aquamarina.model.command.Command;
import ru.aquamarina.model.command.StartCmd;
import ru.aquamarina.model.command.UserInputCmd;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotSupportedCommand;
import ru.aquamarina.model.error.validation.NotAllowedValue;
import ru.aquamarina.model.error.validation.StringParseError;
import ru.aquamarina.util.Result;
import ru.aquamarina.util.UserInputUtil;

public class PoolDepthState implements FsmState {

    public static final String NAME = "PoolDepth";

    private final Logger log = LoggerFactory.getLogger(IndexState.class);

    private final User user;
    private boolean isInvalidInput = false;
    private boolean isInputInRange = true;

    public PoolDepthState(User user) {
        this.user = user;
    }

    @Override
    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
        return switch (command) {
            case UserInputCmd input -> input.asLong()
                    .map(UserInputUtil::validateAboveZero)
                    .map(value -> context.getPoolInfoService()
                            .update(user.getId(),
                                    value,
                                    null,
                                    null,
                                    null,
                                    null
                            )
                    )
                    .mapValue(res -> (FsmState) new PoolDiameterState(user))
                    .or(error -> {
                        return switch (error) {
                            case NotAllowedValue err -> {
                                isInputInRange = false;
                                yield Result.ok(this);
                            }
                            case StringParseError err -> {
                                isInvalidInput = true;
                                yield Result.ok(this);
                            }
                            default -> Result.error(error);
                        };
                    });
            case StartCmd start -> Result.ok(new IndexState(user, true));
            default -> Result.error(new NotSupportedCommand());
        };
    }

    @Override
    public Form getForm(FsmContextHolder context) {
        // TODO: 3/3/26 maybe redo this. don't really like the way of choosing form
        if (isInvalidInput || !isInputInRange) {
            return new InvalidInputForLongForm(user);
        }
        return new PoolDepthForm(user);
    }

    @Override
    public String toString() {
        return NAME;
    }
}
