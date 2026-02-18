package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;
import ru.aquamarina.instruction.InstructionType;
import ru.aquamarina.instruction.PoolType;
import ru.aquamarina.model.entity.User;

import java.io.File;

public record GuideForm(User user, File guide) implements Form {

    @Override
    public void draw(View view) {
        view.drawGuideForm(this);
    }
}
