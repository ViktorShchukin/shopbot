package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;
import ru.aquamarina.model.command.Command;

import java.util.Collection;

public sealed interface Form permits AboutForm, BasketForm, CatalogForm, ForWholesalerForm, IndexForm, OrderForm, PayAndDeliveryForm, ProductAboutForm, ProductInstructionForm {

    void draw(View view);
}
