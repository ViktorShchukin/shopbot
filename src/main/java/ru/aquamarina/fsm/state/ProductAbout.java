package ru.aquamarina.fsm.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.fsm.form.ProductAboutForm;
import ru.aquamarina.model.command.AddProductToBasket;
import ru.aquamarina.model.command.Catalog;
import ru.aquamarina.model.command.Command;
import ru.aquamarina.model.command.Start;
import ru.aquamarina.model.entity.Product;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotSupportedCommand;
import ru.aquamarina.util.Result;

public class ProductAbout implements FsmState {

    public static final String NAME = "ProductAbout";

    private final Logger log = LoggerFactory.getLogger(ProductAbout.class);
    private final Product product;

    public ProductAbout(Product product) {
        this.product = product;
    }

    @Override
    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
        return switch (command) {
            case Catalog ctg -> Result.ok(new ru.aquamarina.fsm.state.Catalog());
            case Start start-> Result.ok(new ru.aquamarina.fsm.state.Index());
            case AddProductToBasket aptb -> context.getProductService()
                    .getByName(aptb.productName())
                    .map(product -> Result.ok(new AddProductToBasketState(product)));
//            case ru.aquamarina.model.command.Index ndx -> Result.ok(new ru.aquamarina.fsm.state.Index());
//            case ru.aquamarina.model.command.ProductAbout pbt -> Result.ok(new )
            default -> Result.error(new NotSupportedCommand());
        };
    }

    @Override
    public Form getForm(FsmContextHolder context) {
        return new ProductAboutForm(product);
    }

    @Override
    public String toString() {
        return NAME + "?" + product.getName();
    }
}
