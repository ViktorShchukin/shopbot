package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;
import ru.aquamarina.model.command.Command;

import java.util.Collection;

public sealed interface Form permits AboutForm, BasketForm, CatalogForm, IndexForm, OrderForm, ProductAboutForm {

    void draw(View view);
}
