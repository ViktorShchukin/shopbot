package ru.aquamarina.fsm.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.fsm.form.DistributionModeForm;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.model.DistributionMode;
import ru.aquamarina.model.command.Command;
import ru.aquamarina.model.command.DeliveryCmd;
import ru.aquamarina.model.command.SelfPickupCmd;
import ru.aquamarina.model.command.StartCmd;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.CanNotDoOrder;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotSupportedCommand;
import ru.aquamarina.util.Result;

public class DistributionModeState implements FsmState {

    public static final String NAME = "DistributionMode";

    private final Logger log = LoggerFactory.getLogger(CatalogState.class);

    private final User user;

    public DistributionModeState(User user) {
        this.user = user;
    }

    @Override
    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
        return switch (command) {
            case DeliveryCmd dlv -> context.getBasketService()
                    .getByUserId(command.getUser().getId())
                    .map(basket -> context.getOrderService().create(user, null, null, DistributionMode.DELIVERY))
                    .map(order -> Result.<FsmState, Error>ok(new OrderAdditionalInfoAddressState(user, order)))
                    .orElseGet(() -> Result.error(new CanNotDoOrder()));
            case SelfPickupCmd spu -> context.getBasketService()
                    .getByUserId(command.getUser().getId())
                    .map(basket -> context.getOrderService().create(user, null, null, DistributionMode.SERLF_PICKUP))
                    .map(order -> Result.<FsmState, Error>ok(new OrderAdditionalInfoPhoneState(user, order)))
                    .orElseGet(() -> Result.error(new CanNotDoOrder()));
            case StartCmd start -> Result.ok(new IndexState(user));
            default -> Result.error(new NotSupportedCommand());
        };
    }

    @Override
    public Form getForm(FsmContextHolder context) {
        return new DistributionModeForm(user);
    }

    @Override
    public String toString() {
        return NAME;
    }
}
