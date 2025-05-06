package ru.aquamarina.fsm.state;

import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.fsm.form.OrderForm;
import ru.aquamarina.model.command.*;
import ru.aquamarina.model.entity.BasketRow;
import ru.aquamarina.model.entity.OrderRow;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotSupportedCommand;
import ru.aquamarina.util.Result;

import java.util.List;

public class OrderState implements FsmState {

    public final static String NAME = "Order";

    private final User user;

    public OrderState(User user) {
        this.user = user;
    }

    @Override
    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
        return switch (command) {
            case StartCmd start -> Result.ok(new IndexState());
            case IndexCmd ndx -> Result.ok(new IndexState());
            default -> Result.error(new NotSupportedCommand());
        };
    }

    @Override
    public Form getForm(FsmContextHolder context) {
        List<OrderRow> rows = context.getOrderService().getOrderRow(user);
        Long totalCost = rows.stream().map(OrderRow::getQuantity).reduce(0L, Long::sum);
        return new OrderForm(rows, totalCost);
    }

    @Override
    public String toString() {
        return NAME;
    }
}
