package ru.aquamarina.fsm;

import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.fsm.state.*;
import ru.aquamarina.model.DistributionMode;
import ru.aquamarina.model.command.Command;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.UnknownState;
import ru.aquamarina.service.OrderService;
import ru.aquamarina.service.UserService;
import ru.aquamarina.util.CommandUtil;
import ru.aquamarina.util.Result;

import java.util.UUID;

@Singleton
public class DefaultFsmRunner implements FsmRunner {

    private static final Logger log = LoggerFactory.getLogger(DefaultFsmRunner.class);

    private final UserService userService;
    private final OrderService orderService;
    private final FsmContextHolder fsmContextHolder;

    public DefaultFsmRunner(UserService userService, OrderService orderService, FsmContextHolder fsmContextHolder) {
        this.userService = userService;
        this.orderService = orderService;
        this.fsmContextHolder = fsmContextHolder;
    }

    @Override
    public Result<Form, Error> execute(Command command) {
        return restoreState(command.getUser())
                .map(state -> state.doWork(fsmContextHolder, command))
                .or(err -> {
                    log.error("error in the state machine: {}", err);
                    return Result.<FsmState, Error>ok(new ErrorState(command.getUser(), err));
                })
                .map(state -> userService.updateState(command.getUser(), state))
                .mapValue(state -> state.getForm(fsmContextHolder));
    }

    private Result<FsmState, Error> restoreState(User user) {
        String caseName = user.getLastState();
        return switch (caseName) {
            case AboutState.NAME -> Result.ok(new AboutState(user));
            case ForWholesalerState.NAME -> Result.ok(new ForWholesalerState(user));
            case PayAndDeliveryState.NAME -> Result.ok(new PayAndDeliveryState(user));
            case BasketState.NAME -> Result.ok(new BasketState(user));
            case CatalogState.NAME -> Result.ok(new CatalogState(user, "/"));
            case IndexState.NAME -> Result.ok(new IndexState(user));
            case InitState.NAME -> Result.ok(new InitState(user));
            case String str when str.contains(OrderState.NAME) -> CommandUtil.parseCmdWithUuidArg(str)
                    .map(id -> orderService.findById(id))
                    .map(order -> Result.ok(new OrderState(user, order)));
            case String str when str.contains(ProductAboutState.NAME) -> {
                long quantity = Long.parseLong(str.split("\\?")[2]);
                yield CommandUtil.parseCmdWithUuidArg(str)
                        .map(id -> fsmContextHolder.getProductService().getById(id))
                        .map(product -> Result.ok(new ProductAboutState(user, product, quantity)));
            }
            case String str when str.contains(ProductInstructionState.NAME) -> CommandUtil.parseCmdWithUuidArg(str)
                    .map(id -> fsmContextHolder.getProductService().getById(id))
                    .map(product -> Result.ok(new ProductInstructionState(user, product)));
            case ErrorState.NAME -> Result.ok(new ErrorState(user));
            case DistributionModeState.NAME -> Result.ok(new DistributionModeState(user));
            case ContactState.NAME -> Result.ok(new ContactState(user));
            case ShopState.NAME -> Result.ok(new ShopState(user));
            case PoolSizeInfoState.NAME -> Result.ok(new PoolSizeInfoState(user));
            case PoolTypeState.NAME -> Result.ok(new PoolTypeState(user));
            case GuideState.NAME ->  Result.ok(new GuideState(user));
            case PoolDepthState.NAME -> Result.ok(new PoolDepthState(user));
            case PoolDiameterState.NAME ->  Result.ok(new PoolDiameterState(user));
            case PoolWidthState.NAME -> Result.ok(new PoolWidthState(user));
            case PoolLengthState.NAME -> Result.ok(new PoolLengthState(user));
            // deprecated
//            case String str when str.contains(OrderAdditionalInfoAddressState.NAME) ->
//                    CommandUtil.parseCmdWithUuidArg(str)
//                            .map(id -> fsmContextHolder.getOrderService().findById(id))
//                            .mapValue(order -> new OrderAdditionalInfoAddressState(user, order));
//            case String str when str.contains(OrderAdditionalInfoPhoneState.NAME) ->
//                    CommandUtil.parseCmdWithUuidArg(str)
//                            .map(id -> fsmContextHolder.getOrderService().findById(id))
//                            .mapValue(order -> new OrderAdditionalInfoPhoneState(user, order));
            case String str when str.contains(OrderAdditionalInfoState.NAME) -> {
                DistributionMode mode = DistributionMode.valueOf(str.split("\\?")[2]);
                yield  CommandUtil.parseCmdWithUuidArg(str)
                        .map(id -> fsmContextHolder.getOrderService().findById(id))
                        .mapValue(order -> new OrderAdditionalInfoState(user, order, mode));
            }
            default -> Result.error(new UnknownState());
        };
    }
}
