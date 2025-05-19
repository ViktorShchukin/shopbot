package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;
import ru.aquamarina.model.command.*;
import ru.aquamarina.model.entity.Product;

import java.util.Collection;
import java.util.List;

public record ProductAboutForm(Product product, long quantity) implements Form {


    @Override
    public void draw(View view) {
        view.drawProductAboutForm(this);
    }
}
