package ru.aquamarina.fsm.state;

import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.fsm.form.AddProductToBasketForm;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.model.command.Command;
import ru.aquamarina.model.command.ProductAbout;
import ru.aquamarina.model.command.Start;
import ru.aquamarina.model.entity.Product;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotSupportedCommand;
import ru.aquamarina.util.Result;

public class AddProductToBasketState implements FsmState {

    public static final String NAME = "AddProductToBasketState";
    private final Product product;

    public AddProductToBasketState(Product product) {
        this.product = product;
    }
    @Override
    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
        return switch (command) {
            case ru.aquamarina.model.command.Index index -> Result.ok(new Index());
            case Start start-> Result.ok(new ru.aquamarina.fsm.state.Index());
            case ProductAbout abt -> Result.ok(new ru.aquamarina.fsm.state.ProductAbout(product));
            default -> Result.error(new NotSupportedCommand());
        };
    }

    @Override
    public Form getForm(FsmContextHolder context) {
        return new AddProductToBasketForm(product);
    }

    @Override
    public String toString() {
        return NAME + "?" + product.getName();
    }
}
