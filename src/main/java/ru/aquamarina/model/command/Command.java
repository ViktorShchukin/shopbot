package ru.aquamarina.model.command;

import ru.aquamarina.model.entity.User;

public sealed interface Command
        permits AboutCmd, AddToBasketCmd, BasketCmd, CatalogCmd, DoOrderCmd, FolderCmd, ForWholesalerCmd, IndexCmd, InstructionCmd, PayAndDeliveryCmd, ProductAboutCmd, QuantityMinusCmd, QuantityPlusCmd, StartCmd {

    User getUser();
}
