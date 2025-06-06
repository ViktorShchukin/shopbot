package ru.aquamarina.fsm.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.fsm.form.ProductInstructionForm;
import ru.aquamarina.model.command.Command;
import ru.aquamarina.model.command.ProductAboutCmd;
import ru.aquamarina.model.command.StartCmd;
import ru.aquamarina.model.entity.BasketRow;
import ru.aquamarina.model.entity.Product;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotSupportedCommand;
import ru.aquamarina.util.Result;

public class ProductInstructionState implements FsmState {

    public static final String NAME = "ProductInstruction";

    private final Logger log = LoggerFactory.getLogger(ProductAboutState.class);

    private final User user;
    private final Product product;

    public ProductInstructionState(User user, Product product) {
        this.user = user;
        this.product = product;
    }

    @Override
    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
        return switch (command) {
            case ProductAboutCmd pbt -> context.getProductService()
                    .getById(pbt.productId())
                    .map(prod -> {
                        Long quantity = context.getBasketService()
                                .getBasketRow(user).stream()
                                .filter(bsk -> bsk.getProductId().equals(prod.getId()))
                                .findFirst()
                                .map(BasketRow::getQuantity)
                                .orElseGet(() -> 0L);
                        return Result.ok(new ProductAboutState(user, prod, quantity));
                    });
            case StartCmd start -> Result.ok(new IndexState(user));
            default -> Result.error(new NotSupportedCommand());
        };
    }

    @Override
    public Form getForm(FsmContextHolder context) {
        return new ProductInstructionForm(product);
    }

    @Override
    public String toString() {
        return NAME + "?" + product.getId().toString();
    }
}
