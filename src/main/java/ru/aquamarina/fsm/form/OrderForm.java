package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;
import ru.aquamarina.model.entity.OrderRow;
import ru.aquamarina.model.entity.User;

import java.util.List;

public record OrderForm(User user, List<OrderRow> rows, Long totalCost) implements Form {

    @Override
    public void draw(View view) {
        view.drawOrderForm(this);
    }
}
