package ru.aquamarina.fsm.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.fsm.form.ProductAboutForm;
import ru.aquamarina.model.command.*;
import ru.aquamarina.model.command.CatalogCmd;
import ru.aquamarina.model.entity.Product;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotSupportedCommand;
import ru.aquamarina.util.Result;

import java.util.Objects;

public class ProductAboutState implements FsmState {

    public static final String NAME = "ProductAbout";

    private final Logger log = LoggerFactory.getLogger(ProductAboutState.class);

    private final User user;
    private final Product product;
    private final long productQuantity;

    public ProductAboutState(User user, Product product, long productQuantity) {
        this.user = user;
        this.product = product;
        this.productQuantity = productQuantity;
    }

    @Override
    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
        return switch (command) {
            case QuantityMinusCmd qm -> {
                assert Objects.equals(qm.productId(), product.getId());
                long resQuantity = productQuantity == 0 ? 0 : productQuantity - 1;
                if (resQuantity == 0) {
                    yield context.getBasketService().deleteFromBasket(user, product)
                            .map(deletedQuantity -> Result.ok(new ProductAboutState(user, product, resQuantity)));
                } else {
                    context.getBasketService().addToBasket(user, product, resQuantity);
                    yield Result.ok(new ProductAboutState(user, product, resQuantity));
                }
            }
            case QuantityPlusCmd qp -> {
                assert Objects.equals(qp.productId(), product.getId());
                long resQuantity = productQuantity + 1;
                context.getBasketService().addToBasket(user, product, resQuantity);
                yield Result.ok(new ProductAboutState(user, product, resQuantity));
            }
            case AddToBasketCmd atb -> {
                context.getBasketService().addToBasket(atb.getUser(), product, productQuantity);
                yield Result.ok(new ProductAboutState(user, product, 0));
            }
            case BasketCmd bsk -> Result.ok(new BasketState(command.getUser()));
            case IndexCmd ndx -> Result.ok(new IndexState(user));
            case CatalogCmd ctg -> Result.ok(new CatalogState(user, product.getPath()));
            case StartCmd start -> Result.ok(new IndexState(user));
            case InstructionCmd inst -> Result.ok(new ProductInstructionState(user, product));
            case DoNothing don -> Result.ok(this);
            default -> Result.error(new NotSupportedCommand());
        };
    }

    @Override
    public Form getForm(FsmContextHolder context) {
        return new ProductAboutForm(user, product, productQuantity);
    }

    @Override
    public String toString() {
        return NAME + "?" + product.getId().toString() + "?" + productQuantity;
    }
}
