package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;
import ru.aquamarina.guide.dto.PoolGuideDto;
import ru.aquamarina.guide.dto.PoolInfoDto;
import ru.aquamarina.model.entity.User;

public record ListOfChemicalsForm(User user, PoolGuideDto poolGuideDto) implements Form {

    @Override
    public void draw(View view) {
        view.drawListOfChemicalsForm(this);
    }
}
