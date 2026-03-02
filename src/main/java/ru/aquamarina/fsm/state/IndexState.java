package ru.aquamarina.fsm.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.fsm.form.IndexForm;
import ru.aquamarina.instruction.PoolType;
import ru.aquamarina.model.command.*;
import ru.aquamarina.model.entity.PoolInfo;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotSupportedCommand;
import ru.aquamarina.util.Result;

import java.util.UUID;

public class IndexState implements FsmState {

    public static final String NAME = "Start";

    private final Logger log = LoggerFactory.getLogger(IndexState.class);

    private final User user;
    private boolean isRestartRequired = false;

    public IndexState(User user) {
        this.user = user;
    }

    public IndexState(User user, boolean isRestartRequired) {
        this(user);
        this.isRestartRequired = isRestartRequired;
    }

    @Override
    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
        return switch (command) {
//            case ContactCmd cnt -> Result.ok(new ContactState(user));
//            case ShopCmd shp -> Result.ok(new ShopState(user));
//            case PoolTypeCmd plt -> Result.ok(new PoolTypeState(user));
            case CircleCmd crl -> {
                PoolInfo info = PoolInfo.of(
                        UUID.randomUUID(),
                        user.getId(),
                        PoolType.CIRCLE
                );
                yield context.getPoolInfoService()
                        .createOrUpdate(info)
                        .mapValue(res -> new PoolDepthState(user));
            }
            case RectangleCmd rec -> Result.ok(new PoolSizeInfoState(user));
            case StartCmd start -> Result.ok(new IndexState(user, true));
            default -> Result.error(new NotSupportedCommand());
        };
    }

    @Override
    public Form getForm(FsmContextHolder context) {
        return new IndexForm(user, isRestartRequired);
    }

    @Override
    public String toString() {
        return NAME;
    }
}
