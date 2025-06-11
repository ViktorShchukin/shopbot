package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;
import ru.aquamarina.model.entity.User;

public record OrderAdditionalInfoPhoneInvalidForm(User user, String invalidPhoneNumber) implements Form {

    @Override
    public void draw(View view) {
        view.OrderAdditionalInfoPhoneInvalidForm(this);
    }
}
