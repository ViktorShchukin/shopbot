package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;
import ru.aquamarina.model.command.Command;
import ru.aquamarina.model.command.FolderCmd;
import ru.aquamarina.model.command.IndexCmd;
import ru.aquamarina.model.command.ProductAboutCmd;
import ru.aquamarina.model.entity.Folder;
import ru.aquamarina.model.entity.Product;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public record CatalogForm(List<Command> commands) implements Form {

    @Override
    public Collection<Command> getCommands() {
        return commands;
    }

    @Override
    public void draw(View view) {
        view.drawCatalogForm(this);
    }
}
