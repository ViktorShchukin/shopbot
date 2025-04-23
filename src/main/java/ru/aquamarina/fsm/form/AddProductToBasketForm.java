package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;
import ru.aquamarina.model.command.Index;
import ru.aquamarina.model.command.ProductAbout;
import ru.aquamarina.model.entity.Product;

import java.util.List;

public record AddProductToBasketForm(Product product) implements Form {
    @Override
    public List<String> getCommands() {
        return List.of(Index.NAME, ProductAbout.NAME + "?" + product.getName());
    }

    @Override
    public void draw(View view) {
        view.drawAddProductToBasketForm(this);
    }
}
