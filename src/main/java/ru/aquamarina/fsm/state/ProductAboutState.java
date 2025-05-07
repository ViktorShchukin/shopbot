package ru.aquamarina.fsm.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.fsm.form.ProductAboutForm;
import ru.aquamarina.model.command.*;
import ru.aquamarina.model.command.CatalogCmd;
import ru.aquamarina.model.entity.Product;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotSupportedCommand;
import ru.aquamarina.util.Result;

public class ProductAboutState implements FsmState {

    public static final String NAME = "ProductAbout";

    private final Logger log = LoggerFactory.getLogger(ProductAboutState.class);
    private final Product product;
    private final long productQuantity;

    public ProductAboutState(Product product, long productQuantity) {
        this.product = product;
        this.productQuantity = productQuantity;
    }

    @Override
    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
        return switch (command) {
            case QuantityMinusCmd qm -> {
                long resQuantity = productQuantity == 0 ? 0 : productQuantity - 1 ;
                yield Result.ok(new ProductAboutState(product, resQuantity));
            }
            case QuantityPlusCmd qp -> Result.ok(new ProductAboutState(product, productQuantity + 1));
            case AddToBasketCmd atb -> {
                context.getBasketService().addToBasket(atb.getUser(), product, productQuantity);
                yield Result.ok(new ProductAboutState(product, 0));
            }
            case BasketCmd bsk -> Result.ok(new BasketState(command.getUser()));
            case IndexCmd ndx -> Result.ok(new IndexState());
            case CatalogCmd ctg -> Result.ok(new CatalogState());
            case StartCmd start -> Result.ok(new IndexState());
            default -> Result.error(new NotSupportedCommand());
        };
    }

    @Override
    public Form getForm(FsmContextHolder context) {
        return new ProductAboutForm(product, productQuantity);
    }

    @Override
    public String toString() {
        return NAME + "?" + product.getName() + "?" + productQuantity;
    }
}
