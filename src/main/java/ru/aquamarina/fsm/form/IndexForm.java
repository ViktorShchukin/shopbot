package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;
import ru.aquamarina.model.command.AboutCmd;
import ru.aquamarina.model.command.CatalogCmd;
import ru.aquamarina.model.command.Command;

import java.util.Collection;
import java.util.List;

public record IndexForm(List<Command> commands) implements Form {

    @Override
    public Collection<Command> getCommands() {
        return commands;
    }

    @Override
    public void draw(View view) {
        view.drawIndexForm(this);
    }
}
