package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;
import ru.aquamarina.model.command.*;
import ru.aquamarina.model.entity.Product;

import java.util.Collection;
import java.util.List;

public record ProductAboutForm(List<Command> commands, Product product, long quantity) implements Form {

    @Override
    public Collection<Command> getCommands() {
        return commands;
    }

    @Override
    public void draw(View view) {
        view.drawProductAboutForm(this);
    }
}
