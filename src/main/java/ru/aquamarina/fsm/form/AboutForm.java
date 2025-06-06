package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;


public record AboutForm() implements Form {

    @Override
    public void draw(View view) {
        view.drawAboutForm(this);
    }
}
