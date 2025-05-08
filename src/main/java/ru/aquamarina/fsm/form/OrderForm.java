package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;
import ru.aquamarina.model.command.Command;
import ru.aquamarina.model.command.IndexCmd;
import ru.aquamarina.model.entity.OrderRow;

import java.util.Collection;
import java.util.List;

public record OrderForm(List<Command> commands, List<OrderRow> rows, Long totalCost) implements Form {
    @Override
    public Collection<Command> getCommands() {
        return commands;
    }

    @Override
    public void draw(View view) {
        view.drawOrderForm(this);
    }
}
