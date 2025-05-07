package ru.aquamarina.fsm;

import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.fsm.state.*;
import ru.aquamarina.model.command.Command;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.UnknownState;
import ru.aquamarina.service.UserService;
import ru.aquamarina.util.Result;

@Singleton
public class DefaultFsmRunner implements FsmRunner {

    private static final Logger log = LoggerFactory.getLogger(DefaultFsmRunner.class);

    private final UserService userService;
    private final FsmContextHolder fsmContextHolder;

    public DefaultFsmRunner(UserService userService, FsmContextHolder fsmContextHolder) {
        this.userService = userService;
        this.fsmContextHolder = fsmContextHolder;
    }

    @Override
    public Result<Form, Error> execute(Command command) {
        return restoreState(command.getUser())
                .map(state -> state.doWork(fsmContextHolder, command))
                .map(state -> userService.updateState(command.getUser(), state))
                .mapValue(state -> state.getForm(fsmContextHolder));
    }

    private Result<FsmState, Error> restoreState(User user) {
        String caseName = user.getLastState();
        return switch (caseName) {
            case AboutState.NAME -> Result.ok(new AboutState());
            case BasketState.NAME -> Result.ok(new BasketState(user));
            case CatalogState.NAME -> Result.ok(new CatalogState());
            case IndexState.NAME -> Result.ok(new IndexState());
            case InitState.NAME -> Result.ok(new InitState());
            case OrderState.NAME -> Result.ok(new OrderState(user));
            case String str when str.contains(ProductAboutState.NAME) -> {
                long quantity = Long.parseLong(str.split("\\?")[2]);
                yield fsmContextHolder.getProductService()
                        .getByName(str.split("\\?")[1])
                        .map(product -> Result.ok(new ProductAboutState(product, quantity)));
            }
            default -> Result.error(new UnknownState());
        };
    }
}
