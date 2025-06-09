package ru.aquamarina.fsm.state;

import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.fsm.form.BasketForm;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.model.command.*;
import ru.aquamarina.model.entity.BasketRow;
import ru.aquamarina.model.entity.Product;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.CanNotDoOrder;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotSupportedCommand;
import ru.aquamarina.util.Result;

import java.util.List;

public class BasketState implements FsmState {

    public static final String NAME = "Basket";

    private final User user;
    // todo make serialization to store the basket or basket id

    public BasketState(User user) {
        this.user = user;
    }

    @Override
    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
        return switch (command) {
            case DoOrderCmd ord -> Result.ok(new DistributionModeState(user));
            case IndexCmd index -> Result.ok(new IndexState(user));
            case CatalogCmd ctg -> Result.ok(new CatalogState(user, "/"));
            case StartCmd start -> Result.ok(new IndexState(user, true));
            case ClearBasketCmd clr -> context.getBasketService().getByUser(user)
                    .map(basket -> {
                        context.getBasketService().clearBasket(basket);
                        return Result.ok(new BasketState(user));
                    });
            case QuantityMinusCmd qm -> context.getBasketService()
                    .getBasketRow(user, qm.productId())
                    .mapValue(BasketRow::getQuantity)
                    .mapValue(quantity -> quantity == 0 ? 0 : quantity - 1)
                    .map(quantity -> {
                        if (quantity == 0) {
                            return context.getBasketService().deleteFromBasket(user, qm.productId());
                        } else {
                            return context.getBasketService().addToBasket(user, qm.productId(), quantity);
                        }
                    })
                    .map(updatedRow -> Result.<FsmState, Error>ok(new BasketState(user)));
            case QuantityPlusCmd qp -> context.getBasketService()
                    .getBasketRow(user, qp.productId())
                    .mapValue(BasketRow::getQuantity)
                    .mapValue(quantity -> quantity + 1)
                    .map(quantity -> context.getBasketService().addToBasket(user, qp.productId(), quantity))
                    .map(updatedRow -> Result.<FsmState, Error>ok(new BasketState(user)));
            case ProductAboutCmd pbt -> context.getProductService().getById(pbt.productId())
                    .map(product -> context.getBasketService()
                            .getBasketRow(user, pbt.productId())
                            .mapValue(basketRow -> new ProductAboutState(user, product, basketRow.getQuantity()))
                    );
            default -> Result.error(new NotSupportedCommand());
        }

                ;
    }

    @Override
    public Form getForm(FsmContextHolder context) {
        List<BasketRow> rows = context.getBasketService().getBasketRow(user);
        Long totalCost = rows.stream()
                .flatMap(basket -> {
                    // todo add cost column to basketRow. Cost can be pulled with join query
                    Long quantity = basket.getQuantity();
                    return context.getProductService()
                            .getById(basket.getProductId())
                            .mapValue(Product::getCost)
                            .mapValue(cost -> cost * quantity)
                            .ok()
                            .stream();
                })
                .reduce(0L, Long::sum);
        return new BasketForm(user, rows, totalCost);
    }

    @Override
    public String toString() {
        return NAME;
    }
}
