package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;
import ru.aquamarina.model.command.Command;

import java.util.List;

public sealed interface Form permits AboutForm, IndexForm {

    /**
     * @return commands that should be placed on the form.
     */
    List<String> getCommands();

    default void draw(View view) {
        view.draw(this);
    }
}
