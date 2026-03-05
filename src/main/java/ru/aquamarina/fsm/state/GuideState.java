package ru.aquamarina.fsm.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.fsm.form.ErrorForm;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.fsm.form.GuideForm;
import ru.aquamarina.guide.GuideType;
import ru.aquamarina.model.command.Command;
import ru.aquamarina.model.command.GuideTypeCmd;
import ru.aquamarina.model.command.IndexCmd;
import ru.aquamarina.model.command.StartCmd;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotSupportedCommand;
import ru.aquamarina.util.Result;
import ru.aquamarina.util.ResultError;
import ru.aquamarina.util.ResultOk;

import java.io.*;

public class GuideState implements FsmState {

    public static final String NAME = "Guide";

    private final Logger log = LoggerFactory.getLogger(GuideState.class);

    private final User user;
    private final GuideType guideType;

    public GuideState(User user, GuideType guideType) {
        this.user = user;
        this.guideType = guideType;
    }

    @Override
    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
        return switch (command) {
            case IndexCmd ndx -> Result.ok(new IndexState(user));
            case GuideTypeCmd gd -> Result.ok(new GuideTypeState(user));
            case StartCmd start -> Result.ok(new IndexState(user, true));
            default -> Result.error(new NotSupportedCommand());
        };
    }

    @Override
    public Form getForm(FsmContextHolder context) {
        return switch (context.getPdfService().getPdf(user)){
            case ResultOk<File, Error> res -> new GuideForm(user, res.result());
            case ResultError<File, Error> err -> new ErrorForm(user);
        };
    }

    @Override
    public String toString() {
        return NAME;
    }
}
