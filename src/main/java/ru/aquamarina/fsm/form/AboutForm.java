package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;
import ru.aquamarina.model.command.Command;
import ru.aquamarina.model.command.IndexCmd;

import java.util.Collection;
import java.util.List;

public record AboutForm() implements Form {

    @Override
    public void draw(View view) {
        view.drawAboutForm(this);
    }
}
