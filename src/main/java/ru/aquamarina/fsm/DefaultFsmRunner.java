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
            case Index.NAME -> Result.ok(new Index());
            // todo think about init state. Now just create user if there is not exist. but what if in future it requires more complicated initialization
            case Init.NAME -> Result.ok(new Init());
            case About.NAME -> Result.ok(new About());
            case Catalog.NAME -> Result.ok(new Catalog());
            case String str when str.contains(ProductAbout.NAME) -> fsmContextHolder.getProductService()
                    .getByName(str.split("\\?")[1])
                    .map(product -> Result.ok(new ProductAbout(product)));
            case String str when str.contains(AddProductToBasketState.NAME) -> fsmContextHolder.getProductService()
                    .getByName(str.split("\\?")[1])
                    .map(product -> Result.ok(new ProductAbout(product)));
            default -> Result.error(new UnknownState());
        };
    }
}
