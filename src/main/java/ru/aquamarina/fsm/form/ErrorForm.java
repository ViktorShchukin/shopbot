package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;

public record ErrorForm() implements Form {

    @Override
    public void draw(View view) {
        view.drawErrorForm(this);
    }
}
