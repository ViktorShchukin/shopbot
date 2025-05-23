package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;

public record ForWholesalerForm() implements Form {

    @Override
    public void draw(View view) {
        view.drawForWholesalerForm(this);
    }
}
