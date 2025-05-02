package ru.aquamarina.fsm.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.fsm.form.ProductAboutForm;
import ru.aquamarina.model.command.*;
import ru.aquamarina.model.command.Catalog;
import ru.aquamarina.model.entity.Product;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotSupportedCommand;
import ru.aquamarina.util.Result;

public class ProductAbout implements FsmState {

    public static final String NAME = "ProductAbout";

    private final Logger log = LoggerFactory.getLogger(ProductAbout.class);
    private final Product product;
    private final long productQuantity;

    public ProductAbout(Product product, long productQuantity) {
        this.product = product;
        this.productQuantity = productQuantity;
    }

    @Override
    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
        return switch (command) {
            case QuantityMinus qm -> {
                long resQuantity = productQuantity == 0 ? 0 : productQuantity - 1 ;
                yield Result.ok(new ProductAbout(product, resQuantity));
            }
            case QuantityPlus qp -> Result.ok(new ProductAbout(product, productQuantity + 1));
            case AddToBasket atb -> {
                context.getBasketservice().addToBasket(atb.getUser(), product, productQuantity);
                yield Result.ok(new ProductAbout(product, 0));
            }
//            case Basket bsk -> Result.ok(new BasketState)
            case Catalog ctg -> Result.ok(new ru.aquamarina.fsm.state.Catalog());
            case Start start -> Result.ok(new ru.aquamarina.fsm.state.Index());
            case ru.aquamarina.model.command.Index ndx -> Result.ok(new ru.aquamarina.fsm.state.Index());
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
