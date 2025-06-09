package ru.aquamarina.fsm.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.fsm.form.OrderAdditionalInfoAddressForm;
import ru.aquamarina.model.command.*;
import ru.aquamarina.model.entity.*;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotSupportedCommand;
import ru.aquamarina.util.Result;

public class OrderAdditionalInfoAddressState implements FsmState {

    public static final String NAME = "AdditionalInfoAddressState";

    private final Logger log = LoggerFactory.getLogger(CatalogState.class);

    private final User user;
    private final Order order;

    public OrderAdditionalInfoAddressState(User user, Order order) {
        this.user = user;
        this.order = order;
    }

    @Override
    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
        return switch (command) {
            case OrderAdditionalInfoAddressCmd cmd -> context.getOrderService()
                    .update(order, null, cmd.address(), null)
                    .mapValue(order1 -> new OrderAdditionalInfoPhoneState(user, order));
            case StartCmd start -> Result.ok(new IndexState(user, true));
            default -> Result.error(new NotSupportedCommand());
        };
    }

    @Override
    public Form getForm(FsmContextHolder context) {
        return new OrderAdditionalInfoAddressForm(user);
    }

    @Override
    public String toString() {
        return new StringBuilder(NAME).append("?").append(order.getId().toString()).toString();
    }
}
