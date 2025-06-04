package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;
import ru.aquamarina.model.entity.Product;

public record ProductInstructionForm(Product product) implements Form {

    @Override
    public void draw(View view) {
        view.drawProductInstructionForm(this);
    }
}
