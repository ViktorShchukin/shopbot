package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;
import ru.aquamarina.model.command.Command;

import java.util.Collection;

public sealed interface Form permits AboutForm, BasketForm, CatalogForm, IndexForm, OrderForm, ProductAboutForm {

    /**
     * @return commands that should be placed on the form.
     */
    Collection<Command> getCommands();

    void draw(View view);
}
