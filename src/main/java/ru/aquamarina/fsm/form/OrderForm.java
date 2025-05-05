package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;
import ru.aquamarina.model.command.IndexCmd;

import java.util.List;

public final class OrderForm implements Form {
    @Override
    public List<String> getCommands() {
        return List.of(IndexCmd.NAME);
    }

    @Override
    public void draw(View view) {
        view.drawOrderForm(this);
    }
}
