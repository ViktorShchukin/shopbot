package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;
import ru.aquamarina.model.command.Index;

import java.util.List;

public final class OrderForm implements Form {
    @Override
    public List<String> getCommands() {
        return List.of(Index.NAME);
    }

    @Override
    public void draw(View view) {
        view.drawOrderForm(this);
    }
}
