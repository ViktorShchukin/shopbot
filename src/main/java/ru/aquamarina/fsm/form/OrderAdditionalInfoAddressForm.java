package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;
import ru.aquamarina.model.entity.User;

public record OrderAdditionalInfoAddressForm(User user) implements Form {
    @Override
    public void draw(View view) {
        view.drawOrderAdditionalInfoAddressForm(this);
    }
}
