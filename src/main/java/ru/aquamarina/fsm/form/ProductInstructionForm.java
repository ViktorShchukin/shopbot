package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;
import ru.aquamarina.model.entity.Product;
import ru.aquamarina.model.entity.User;

public record ProductInstructionForm(User user, Product product) implements Form {

    @Override
    public void draw(View view) {
        view.drawProductInstructionForm(this);
    }
}
