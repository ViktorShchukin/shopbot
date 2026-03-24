package ru.aquamarina.fsm.state;

import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.fsm.form.OrderForm;
import ru.aquamarina.model.command.*;
import ru.aquamarina.model.entity.*;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotSupportedCommand;
import ru.aquamarina.util.Result;

import java.util.List;

public class OrderState implements FsmState {

    public final static String NAME = "Order";

    private final User user;
    private final Order order;

    public OrderState(User user, Order order) {
        this.user = user;
        this.order = order;
    }

    @Override
    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
        return switch (command) {
            case IndexCmd ndx -> Result.ok(new IndexState(user));
            case StartCmd start -> Result.ok(new IndexState(user, true));
            case CatalogCmd ctg -> Result.ok(new CatalogState(user, "/"));
            default -> Result.error(new NotSupportedCommand());
        };
    }

    @Override
    public Form getForm(FsmContextHolder context) {
        List<OrderRow> rows = context.getOrderService().getOrderRow(order);
        Long totalCost = rows.stream()
                .flatMap(orderRow -> {
                    // todo add cost column to basketRow. Cost can be pulled with join query
                    Long quantity = orderRow.getQuantity();
                    return context.getProductService()
                            .getById(orderRow.getProductId())
                            .mapValue(Product::getCost)
                            .mapValue(cost -> cost * quantity)
                            .ok()
                            .stream();
                })
                .reduce(0L, Long::sum);
        return new OrderForm(user, rows, totalCost);
    }

    @Override
    public String toString() {
        return NAME + "?" + order.getId().toString();
    }
}
