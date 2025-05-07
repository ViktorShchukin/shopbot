package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;
import ru.aquamarina.model.command.DoOrderCmd;
import ru.aquamarina.model.command.IndexCmd;
import ru.aquamarina.model.entity.BasketRow;
import ru.aquamarina.model.entity.Product;

import java.util.List;

public record BasketForm(List<BasketRow> rows, Long totalCost) implements Form {

    @Override
    public List<String> getCommands() {
        return List.of(
                IndexCmd.NAME,
                DoOrderCmd.NAME
        );
    }

    @Override
    public void draw(View view) {
        view.drawBasketForm(this);
    }
}
