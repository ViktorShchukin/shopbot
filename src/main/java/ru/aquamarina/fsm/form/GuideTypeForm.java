package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;
import ru.aquamarina.model.entity.User;

public record GuideTypeForm(User user) implements Form {
    @Override
    public void draw(View view) {
        view.drawGuideTypeForm(this);
    }
}
