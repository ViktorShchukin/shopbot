package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;
import ru.aquamarina.model.command.AboutCmd;
import ru.aquamarina.model.command.CatalogCmd;

import java.util.List;

public final class IndexForm implements Form {

    @Override
    public List<String> getCommands() {
        return List.of(AboutCmd.NAME, CatalogCmd.NAME);
    }

    @Override
    public void draw(View view) {
        view.drawIndexForm(this);
    }
}
