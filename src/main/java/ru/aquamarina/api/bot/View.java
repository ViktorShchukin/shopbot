package ru.aquamarina.api.bot;

import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.model.error.Error;

public interface View<DrCon extends DrawContext> {

    void draw(DrCon drawContext, Form form);

    void drawError(DrCon drawContext, Error error);
}
