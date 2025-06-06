package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;

public record IndexForm() implements Form {

    @Override
    public void draw(View view) {
        view.drawIndexForm(this);
    }
}
