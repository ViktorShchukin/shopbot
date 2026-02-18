package ru.aquamarina.fsm.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.fsm.form.AboutForm;
import ru.aquamarina.fsm.form.ErrorForm;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.fsm.form.GuideForm;
import ru.aquamarina.instruction.DefaultPdfFactory;
import ru.aquamarina.instruction.InstructionType;
import ru.aquamarina.instruction.PdfFactory;
import ru.aquamarina.instruction.PoolType;
import ru.aquamarina.model.command.Command;
import ru.aquamarina.model.command.ContactCmd;
import ru.aquamarina.model.command.IndexCmd;
import ru.aquamarina.model.command.StartCmd;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotSupportedCommand;
import ru.aquamarina.util.Result;
import ru.aquamarina.util.ResultError;
import ru.aquamarina.util.ResultOk;

import java.io.File;

public class GuideState implements FsmState {

    public static final String NAME = "Guide";

    private final Logger log = LoggerFactory.getLogger(IndexState.class);

    private final User user;

    public GuideState(User user) {
        this.user = user;
    }

    @Override
    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
        return switch (command) {
            case IndexCmd ndx -> Result.ok(new IndexState(user));
            case StartCmd start-> Result.ok(new IndexState(user, true));
            default -> Result.error(new NotSupportedCommand());
        };
    }

    @Override
    public Form getForm(FsmContextHolder context) {
        PdfFactory guideFactory = new DefaultPdfFactory(PoolType.RECTANGLE, 20, InstructionType.START_THE_USAGE);
        return switch (guideFactory.getPdf()) {
            case ResultOk<File, Error> res -> new GuideForm(user, res.result());
            case ResultError<File, Error> err -> {
                log.error("error during pdf creation: {}", err.err().toString());
                yield new ErrorForm(user);
            }
        };
    }

    @Override
    public String toString() {
        return NAME;
    }
}
