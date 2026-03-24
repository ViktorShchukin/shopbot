package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;
import ru.aquamarina.model.entity.BasketRow;
import ru.aquamarina.model.entity.User;

import java.util.List;

public record BasketForm(User user, List<BasketRow> rows, Long totalCost) implements Form {

    @Override
    public void draw(View view) {
        view.drawBasketForm(this);
    }
}
