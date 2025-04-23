package ru.aquamarina.api.bot;

import ru.aquamarina.fsm.form.*;
import ru.aquamarina.model.error.Error;

public interface View {

    void drawAboutForm(AboutForm form);

    void drawIndexForm(IndexForm form);

    void draw(Error error);
}
