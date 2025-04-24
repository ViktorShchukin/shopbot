package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;
import ru.aquamarina.model.command.*;
import ru.aquamarina.model.entity.Product;

import java.util.List;

public record ProductAboutForm(Product product, long quantity) implements Form {

    @Override
    public List<String> getCommands() {
        return List.of(
                QuantityMinus.NAME,
                QuantityPlus.NAME,
                AddToBasket.NAME,
                Basket.NAME,
                Index.NAME,
                Catalog.NAME,
                Instruction.NAME
                );
    }

    @Override
    public void draw(View view) {
        view.drawProductAboutForm(this);
    }
}
