package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;
import ru.aquamarina.model.command.Command;
import ru.aquamarina.model.command.DoOrderCmd;
import ru.aquamarina.model.command.IndexCmd;
import ru.aquamarina.model.entity.BasketRow;
import ru.aquamarina.model.entity.Product;

import java.util.Collection;
import java.util.List;

public record BasketForm(List<Command> commands, List<BasketRow> rows, Long totalCost) implements Form {

    @Override
    public Collection<Command> getCommands() {
        return commands;
    }

    @Override
    public void draw(View view) {
        view.drawBasketForm(this);
    }
}
