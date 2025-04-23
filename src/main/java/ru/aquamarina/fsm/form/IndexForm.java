package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;
import ru.aquamarina.model.command.About;

import java.util.List;

public final class IndexForm implements Form {

    @Override
    public List<String> getCommands() {
        return List.of(About.NAME);
    }

    @Override
    public void draw(View view) {
        view.drawIndexForm(this);
    }
}
