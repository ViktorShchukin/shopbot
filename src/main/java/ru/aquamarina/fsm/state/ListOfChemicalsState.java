package ru.aquamarina.fsm.state;

import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.fsm.form.ErrorForm;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.fsm.form.IndexForm;
import ru.aquamarina.fsm.form.ListOfChemicalsForm;
import ru.aquamarina.guide.PoolGuideCalculatorDefault;
import ru.aquamarina.guide.PoolType;
import ru.aquamarina.guide.dto.PoolGuideDto;
import ru.aquamarina.mapper.PoolInfoTool;
import ru.aquamarina.model.command.*;
import ru.aquamarina.model.entity.PoolInfo;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotSupportedCommand;
import ru.aquamarina.util.Result;
import ru.aquamarina.util.ResultError;
import ru.aquamarina.util.ResultOk;

import java.util.UUID;

public class ListOfChemicalsState implements FsmState {

    public static final String NAME = "ListOfChemicals";

    private final Logger log = LoggerFactory.getLogger(IndexState.class);
    private final PoolInfoTool poolInfoTool = Mappers.getMapper(PoolInfoTool.class);
    private final User user;

    public ListOfChemicalsState(User user) {
        this.user = user;
    }


    @Override
    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
        return switch (command) {
            case ContactCmd cmd -> Result.ok(new ContactListOfChemicalsState(user));
            case IndexCmd ndx -> Result.ok(new IndexState(user));
            case GuideTypeCmd gd -> Result.ok(new GuideTypeState(user));
            case StartCmd start -> Result.ok(new IndexState(user, true));
            default -> Result.error(new NotSupportedCommand());
        };
    }

    @Override
    public Form getForm(FsmContextHolder context) {
        Result<PoolGuideDto, Error> result = context.getPoolInfoService()
                .getPoolInfoByUserId(user.getId())
                .mapValue(poolInfoTool::map)
                .mapValue(poolInfoDto -> PoolGuideCalculatorDefault.of(poolInfoDto).evaluate());

        return switch (result) {
            case ResultOk<PoolGuideDto, Error> res -> new ListOfChemicalsForm(user, res.result());
            case ResultError<PoolGuideDto, Error> err -> new ErrorForm(user);
        };
    }

    @Override
    public String toString() {
        return NAME;
    }
}
