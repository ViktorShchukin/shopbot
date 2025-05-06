package ru.aquamarina.fsm.state;

import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.fsm.form.BasketForm;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.model.command.Command;
import ru.aquamarina.model.command.DoOrderCmd;
import ru.aquamarina.model.command.IndexCmd;
import ru.aquamarina.model.command.StartCmd;
import ru.aquamarina.model.entity.Basket;
import ru.aquamarina.model.entity.BasketRow;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.CanNotDoOrder;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotSupportedCommand;
import ru.aquamarina.util.Result;

import java.util.List;

public class BasketState implements FsmState {

    public static final String NAME = "Basket";

    private final User user;
    // todo make serialization to store the basket or basket id
//    private final Basket basket;

    public BasketState(User user) {
        this.user = user;
    }

    @Override
    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
        return switch (command) {
            case DoOrderCmd ord -> context.getBasketService()
                    .getByUserId(command.getUser().getId())
                    .map(basket -> context.getOrderService().create(basket))
                    .map(order -> Result.<FsmState, Error>ok(new OrderState(command.getUser())))
                    .orElseGet(() -> Result.error(new CanNotDoOrder()));
            case IndexCmd index -> Result.ok(new IndexState());
            case StartCmd start -> Result.ok(new IndexState());
            default -> Result.error(new NotSupportedCommand());
        };
    }

    @Override
    public Form getForm(FsmContextHolder context) {
        List<BasketRow> rows = context.getBasketService().getBasketRow(user);
        Long totalCost = rows.stream().map(BasketRow::getQuantity).reduce(0L, Long::sum);
        return new BasketForm(rows, totalCost);
    }

    @Override
    public String toString() {
        return NAME;
    }
}
