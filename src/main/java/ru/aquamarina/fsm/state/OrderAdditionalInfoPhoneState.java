package ru.aquamarina.fsm.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.fsm.form.OrderAdditionalInfoPhoneForm;
import ru.aquamarina.fsm.form.OrderAdditionalInfoPhoneInvalidForm;
import ru.aquamarina.model.command.*;
import ru.aquamarina.model.entity.*;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.InvalidPhoneNumber;
import ru.aquamarina.model.error.NotSupportedCommand;
import ru.aquamarina.util.Result;
import ru.aquamarina.util.ResultError;

import java.util.regex.Pattern;

@Deprecated
public class OrderAdditionalInfoPhoneState implements FsmState {

    public static final String NAME = "AdditionalInfoPhone";
    private static final Pattern phoneNumberPattern = Pattern.compile("\\+7([\\s\\-]?\\d){10}");

    private final Logger log = LoggerFactory.getLogger(CatalogState.class);

    private final User user;
    private final Order order;
    private final String invalidPhoneNumber;
    private final boolean isFirst;

    public OrderAdditionalInfoPhoneState(User user, Order order) {
        this.user = user;
        this.order = order;
        this.isFirst = true;
        this.invalidPhoneNumber = null;
    }

    public OrderAdditionalInfoPhoneState(User user, Order order, boolean isFirst, String invalidPhoneNumber) {
        this.user = user;
        this.order = order;
        this.isFirst = isFirst;
        this.invalidPhoneNumber = invalidPhoneNumber;
    }

    @Override
    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
        return switch (command) {
//            case OrderAdditionalInfoPhoneCmd cmd -> {
//                String trimmedPhone = cmd.phoneNumber().trim();
//                var validRes = validateNumber(trimmedPhone);
//                if (validRes instanceof ResultError<String, Error> error) {
//                    yield Result.ok(new OrderAdditionalInfoPhoneState(user, order, false, trimmedPhone));
//                }
//
//                yield context.getOrderService()
//                        .update(order, trimmedPhone, null, null)
//                        .map(order1 -> context.getBasketService().getByUser(user))
//                        .map(basket -> context.getOrderService().fillTheOrderAndClearBasket(order, basket))
//                        .mapValue(order1 -> {
//                            context.getTelegramService().notifySeller(order);
//                            return order;
//                        })
//                        .mapValue(order1 -> new OrderState(user, order));
//            }
            case StartCmd start -> Result.ok(new IndexState(user, true));
            default -> Result.error(new NotSupportedCommand());
        };
    }

    @Override
    public Form getForm(FsmContextHolder context) {
        if (isFirst) {
            return new OrderAdditionalInfoPhoneForm(user);
        } else {
            return new OrderAdditionalInfoPhoneInvalidForm(user, invalidPhoneNumber);
        }
    }

    @Override
    public String toString() {
        return new StringBuilder(NAME).append("?").append(order.getId().toString()).toString();
    }

    private Result<String, Error> validateNumber(String phoneNumber) {
        if (phoneNumberPattern.matcher(phoneNumber).matches()) {
            return Result.ok(phoneNumber);
        } else {
            return Result.error(new InvalidPhoneNumber(phoneNumber));
        }
    }

    private record PhoneDto(boolean isValid, String phoneNumber) {
    }
}
