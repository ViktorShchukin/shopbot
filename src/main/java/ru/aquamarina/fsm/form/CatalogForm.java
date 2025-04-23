package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;
import ru.aquamarina.model.command.Index;
import ru.aquamarina.model.command.ProductAbout;
import ru.aquamarina.model.entity.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class CatalogForm implements Form {

    private final List<Product> products;

    public CatalogForm(List<Product> products) {
        this.products = products;
    }

    @Override
    public List<String> getCommands() {
        List<String> commands = products.stream()
                .map(Product::getName)
                .map(name -> ProductAbout.NAME + "?" + name)
                .collect(Collectors.toCollection(ArrayList::new));
        commands.add(Index.NAME);
        return commands;
    }

    @Override
    public void draw(View view) {
        view.drawCatalogForm(this);
    }

    public List<Product> getProducts() {
        return products;
    }
}
