package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;
import ru.aquamarina.model.command.*;
import ru.aquamarina.model.entity.Product;

import java.util.List;

public record ProductAboutForm(Product product, long quantity) implements Form {

    @Override
    public List<String> getCommands() {
        return List.of(
                QuantityMinusCmd.NAME,
                QuantityPlusCmd.NAME,
                AddToBasketCmd.NAME,
                BasketCmd.NAME,
                IndexCmd.NAME,
                CatalogCmd.NAME,
                InstructionCmd.NAME
                );
    }

    @Override
    public void draw(View view) {
        view.drawProductAboutForm(this);
    }
}
