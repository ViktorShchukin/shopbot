package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;
import ru.aquamarina.guide.GuideType;
import ru.aquamarina.model.entity.User;

import java.io.File;

public record GuideForm(User user, File guide, GuideType guideType) implements Form {

    @Override
    public void draw(View view) {
        view.drawGuideForm(this);
    }
}
