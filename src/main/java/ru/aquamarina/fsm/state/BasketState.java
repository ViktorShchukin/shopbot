package ru.aquamarina.fsm.state;

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
