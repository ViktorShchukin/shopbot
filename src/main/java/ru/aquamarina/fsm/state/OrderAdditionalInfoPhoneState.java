package ru.aquamarina.fsm.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.fsm.form.OrderAdditionalInfoPhoneForm;
import ru.aquamarina.model.command.*;
import ru.aquamarina.model.entity.*;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotSupportedCommand;
import ru.aquamarina.util.Result;

public class OrderAdditionalInfoPhoneState implements FsmState {

    public static final String NAME = "AdditionalInfoPhone";

    private final Logger log = LoggerFactory.getLogger(CatalogState.class);

    private final User user;
    private final Order order;

    public OrderAdditionalInfoPhoneState(User user, Order order) {
        this.user = user;
        this.order = order;
    }

    @Override
    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
        return switch (command) {
            case OrderAdditionalInfoPhoneCmd cmd -> context.getOrderService()
                    .update(order, cmd.phoneNumber(), null, null)
                    .map(order1 -> context.getBasketService().getByUser(user))
                    .map(basket -> context.getOrderService().fillTheOrderAndClearBasket(order, basket))
                    .mapValue(order1 -> {
                        context.getTelegramService().notifySeller(order);
                        return order;
                    })
                    .mapValue(order1 -> new OrderState(user, order));
            case StartCmd start -> Result.ok(new IndexState(user));
            default -> Result.error(new NotSupportedCommand());
        };
    }

    @Override
    public Form getForm(FsmContextHolder context) {
        return new OrderAdditionalInfoPhoneForm(user);
    }

    @Override
    public String toString() {
        return new StringBuilder(NAME).append("?").append(order.getId().toString()).toString();
    }
}
