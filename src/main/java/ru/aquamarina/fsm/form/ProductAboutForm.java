package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;
import ru.aquamarina.model.command.AddProductToBasket;
import ru.aquamarina.model.command.Catalog;
import ru.aquamarina.model.command.Index;
import ru.aquamarina.model.entity.Product;

import java.util.List;

public record ProductAboutForm(Product product) implements Form {

    @Override
    public List<String> getCommands() {
        return List.of(Catalog.NAME, AddProductToBasket.NAME + "?" + product.getName());
    }

    @Override
    public void draw(View view) {
        view.drawProductAboutForm(this);
    }
}
