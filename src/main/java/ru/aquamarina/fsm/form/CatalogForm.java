package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;
import ru.aquamarina.model.entity.Folder;
import ru.aquamarina.model.entity.Product;
import ru.aquamarina.model.entity.User;

import java.util.List;

public record CatalogForm(User user, List<Product> products, List<Folder> folders, String path) implements Form {

    @Override
    public void draw(View view) {
        view.drawCatalogForm(this);
    }
}
