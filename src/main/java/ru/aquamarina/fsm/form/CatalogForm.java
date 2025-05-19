package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;
import ru.aquamarina.model.entity.Folder;
import ru.aquamarina.model.entity.Product;

import java.util.List;

public record CatalogForm(List<Product> products, List<Folder> folders) implements Form {

    @Override
    public void draw(View view) {
        view.drawCatalogForm(this);
    }
}
