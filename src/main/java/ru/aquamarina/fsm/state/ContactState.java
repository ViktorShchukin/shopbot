package ru.aquamarina.fsm.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.fsm.form.AboutForm;
import ru.aquamarina.fsm.form.ContactForm;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.guide.GuideType;
import ru.aquamarina.model.command.*;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotSupportedCommand;
import ru.aquamarina.util.Result;

public class ContactState implements FsmState {

    public static final String NAME = "Contact";

    private final Logger log = LoggerFactory.getLogger(IndexState.class);

    private final User user;

    public ContactState(User user) {
        this.user = user;
    }

    @Override
    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
        return switch (command) {
            case BackCmd cmd -> Result.ok(new GuideState(user, GuideType.STEP_BY_STEP, false));
            case AboutCmd ndx -> Result.ok(new AboutState(user));
            case ForWholesalerCmd wls -> Result.ok(new ForWholesalerState(user));
            case IndexCmd index -> Result.ok(new IndexState(user));
            case StartCmd start -> Result.ok(new IndexState(user, true));
            default -> Result.error(new NotSupportedCommand());
        };
    }

    @Override
    public Form getForm(FsmContextHolder context) {
        return new ContactForm(user);
    }

    @Override
    public String toString() {
        return NAME;
    }
}
