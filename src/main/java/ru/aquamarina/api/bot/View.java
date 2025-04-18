package ru.aquamarina.api.bot;

import ru.aquamarina.fsm.form.AboutForm;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.fsm.form.IndexForm;
import ru.aquamarina.model.error.Error;

public interface View {

    default <T extends Form> void draw(T form) {
        switch (form) {
            case AboutForm f -> drawAboutForm(f);
            case IndexForm f -> drawIndexForm(f);
        }
    }

    void drawAboutForm(AboutForm form);

    void drawIndexForm(IndexForm form);

    void draw(Error error);
}
