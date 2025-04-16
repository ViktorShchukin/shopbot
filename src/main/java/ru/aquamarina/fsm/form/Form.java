package ru.aquamarina.fsm.form;

import ru.aquamarina.model.command.Command;

import java.util.List;

public interface Form {

    /**
     * @return commands that should be placed on the form.
     */
    List<String> getCommands();
}
