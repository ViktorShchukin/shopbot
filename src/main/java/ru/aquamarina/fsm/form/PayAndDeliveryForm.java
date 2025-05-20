package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;

public record PayAndDeliveryForm() implements Form {

    @Override
    public void draw(View view) {
        view.drawPayAndDeliveryFormForm(this);
    }
}
