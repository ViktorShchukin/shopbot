package ru.aquamarina.fsm.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.fsm.form.IndexForm;
import ru.aquamarina.model.command.Command;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.util.Result;

public class Index implements FsmState {

    public static final String NAME = "Start";

    private final Logger log = LoggerFactory.getLogger(Index.class);

    @Override
    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
        // todo end realization
        return Result.ok(new About());
    }

    @Override
    public Form getForm() {
        return new IndexForm();
    }
}
