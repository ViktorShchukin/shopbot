package ru.aquamarina.fsm.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.fsm.form.CatalogForm;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.model.command.Command;
import ru.aquamarina.model.command.Index;
import ru.aquamarina.model.command.ProductAbout;
import ru.aquamarina.model.command.Start;
import ru.aquamarina.model.entity.Product;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotSupportedCommand;
import ru.aquamarina.util.Result;

import java.util.List;

public class Catalog implements FsmState {

    public static final String NAME = "Catalog";

    private final Logger log = LoggerFactory.getLogger(Catalog.class);

    @Override
    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
        return switch (command) {
            case Index ndx -> Result.ok(new ru.aquamarina.fsm.state.Index());
            case ProductAbout pbt -> context.getProductService()
                    .getByName(pbt.productName())
                    .map(product -> Result.ok(new ru.aquamarina.fsm.state.ProductAbout(product)));
            case Start start-> Result.ok(new ru.aquamarina.fsm.state.Index());
            default -> Result.error(new NotSupportedCommand());
        };
    }

    @Override
    public Form getForm(FsmContextHolder context) {
        List<Product> products = context.getProductService().getAll();
        return new CatalogForm(products);
    }

    @Override
    public String toString() {
        return NAME;
    }
}
