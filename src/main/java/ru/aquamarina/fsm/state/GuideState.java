package ru.aquamarina.fsm.state;

import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.fsm.form.*;
import ru.aquamarina.guide.GuideType;
import ru.aquamarina.guide.PoolGuideCalculatorDefault;
import ru.aquamarina.guide.dto.PoolGuideDto;
import ru.aquamarina.mapper.PoolInfoTool;
import ru.aquamarina.model.command.*;
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

    private final PoolInfoTool poolInfoTool = Mappers.getMapper(PoolInfoTool.class);
    private final User user;
    private final GuideType guideType;
    private final boolean needGuideFile;
    private final boolean needListOfChemicals;

    public GuideState(User user, GuideType guideType, boolean needGuideFile) {
        this.user = user;
        this.guideType = guideType;
        this.needGuideFile = needGuideFile;
        this.needListOfChemicals = false;
    }

    private GuideState(User user, GuideType guideType, boolean needGuideFile, boolean needListOfChemicals) {
        this.user = user;
        this.guideType = guideType;
        this.needGuideFile = needGuideFile;
        this.needListOfChemicals = needListOfChemicals;
    }

    @Override
    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
        return switch (command) {
            case ContactCmd cmd -> Result.ok(new ContactState(user));
            case ListOfChemicalsCmd lof -> Result.ok(new GuideState(user, guideType, needGuideFile, true));
            case IndexCmd ndx -> Result.ok(new IndexState(user));
            case GuideTypeCmd gd -> Result.ok(new GuideTypeState(user));
            case StartCmd start -> Result.ok(new IndexState(user, true));
            default -> Result.error(new NotSupportedCommand());
        };
    }

    @Override
    public Form getForm(FsmContextHolder context) {
        if (!needGuideFile){
            return new GuideWithoutFileForm(user);
        }
        if (needListOfChemicals){
            Result<PoolGuideDto, Error> result = context.getPoolInfoService()
                    .getPoolInfoByUserId(user.getId())
                    .mapValue(poolInfoTool::map)
                    .mapValue(poolInfoDto -> PoolGuideCalculatorDefault.of(poolInfoDto).evaluate());

            return switch (result) {
                case ResultOk<PoolGuideDto, Error> res -> new ListOfChemicalsForm(user, res.result());
                case ResultError<PoolGuideDto, Error> err -> new ErrorForm(user);
            };
        }
        return switch (context.getPdfService().getPdf(user, guideType)){
            case ResultOk<File, Error> res -> new GuideForm(user, res.result(), guideType);
            case ResultError<File, Error> err -> new ErrorForm(user);
        };
    }

    @Override
    public String toString() {
        return NAME;
    }
}
