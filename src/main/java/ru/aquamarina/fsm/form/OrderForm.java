package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;
import ru.aquamarina.model.entity.OrderRow;

import java.util.List;

public record OrderForm(List<OrderRow> rows, Long totalCost) implements Form {

    @Override
    public void draw(View view) {
        view.drawOrderForm(this);
    }
}
