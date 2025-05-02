package ru.aquamarina.fsm.state;

import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.model.command.Command;
import ru.aquamarina.model.command.Start;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotSupportedCommand;
import ru.aquamarina.util.Result;

//public class BasketState implements FsmState {
//
//    @Override
//    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
//        return switch (command) {
//            case ru.aquamarina.model.command.Index index -> Result.ok(new Index());
//            case Start start-> Result.ok(new ru.aquamarina.fsm.state.Index());
//            default -> Result.error(new NotSupportedCommand());
//        };
//    }
//
//    @Override
//    public Form getForm(FsmContextHolder context) {
//        context.getBasketservice().getBasketRowByUserId()
//        return new BasketForm();
//    }
//}
