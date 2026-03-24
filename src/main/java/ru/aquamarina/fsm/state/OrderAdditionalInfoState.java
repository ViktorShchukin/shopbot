package ru.aquamarina.fsm.state;

import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.fsm.form.OrderAdditionalInfoAddressForm;
import ru.aquamarina.fsm.form.OrderAdditionalInfoPhoneForm;
import ru.aquamarina.model.DistributionMode;
import ru.aquamarina.model.command.Command;
import ru.aquamarina.model.command.StartCmd;
import ru.aquamarina.model.command.UserInputCmd;
import ru.aquamarina.model.entity.Order;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotSupportedCommand;
import ru.aquamarina.util.Result;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderAdditionalInfoState implements FsmState {

    public static final String NAME = "AddInfo";
    /**
     * +79281174937
     * 89281174937
     * +7 928 117 49 37
     * 8 928 117 49 37
     * +7 928-117-49-37
     * 8 928-117-49-37
     * +7_928 117 49 37
     * 8 928_1174937
     * 9281174937
     */
    private static final Pattern phoneNumberPattern = Pattern.compile("(?:.*(?<phoneNumber>\\+?(?:[\\s\\-_]?\\d){11}|(?:[\\s\\-_]?\\d){10}).*)");

    private final User user;
    private final Order order;
    private final DistributionMode mode;

    public OrderAdditionalInfoState(User user, Order order, DistributionMode mode) {
        this.user = user;
        this.order = order;
        this.mode = mode;
    }

    @Override
    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
        return switch (command) {
            case UserInputCmd cmd -> processInput(cmd.input())
                    .map(dto -> context.getOrderService().update(order, dto.phoneNumber(), dto.address(), null, dto.input()))
                    .map(order1 -> context.getBasketService().getByUser(user))
                        .map(basket -> context.getOrderService().fillTheOrderAndClearBasket(order, basket))
                        .mapValue(order1 -> {
                            context.getTelegramService().notifySeller(order);
                            return order;
                        })
                        .mapValue(order1 -> new OrderState(user, order));
            case StartCmd start -> Result.ok(new IndexState(user, true));
            default -> Result.error(new NotSupportedCommand());
        };
    }

    @Override
    public Form getForm(FsmContextHolder context) {
        return switch (mode) {
            case DELIVERY -> new OrderAdditionalInfoAddressForm(user);
            case SERLF_PICKUP -> new OrderAdditionalInfoPhoneForm(user);
        };
    }

    @Override
    public String toString() {
        return new StringBuilder(NAME).append("?").append(order.getId()).append("?").append(mode.toString()).toString();
    }

    private Result<InputDto, Error> processInput(String input) {
        String phoneNumber;
        Matcher matcherPhone = phoneNumberPattern.matcher(input);
        try {
             phoneNumber = matcherPhone.group("phoneNumber"); // todo make phoneNumber as constant field. not magic value
        } catch (Exception e) {
            // todo add logging
            phoneNumber = null;
        }
        String address = input;
        // todo check for empty input.
        return Result.ok(new InputDto(phoneNumber, address, input));
    }

    private static record InputDto(String phoneNumber, String address, String input) {
    }
}
