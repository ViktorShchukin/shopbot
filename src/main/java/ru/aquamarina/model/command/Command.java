package ru.aquamarina.model.command;

import ru.aquamarina.model.entity.User;

public sealed interface Command
        permits AboutCmd, AddToBasketCmd, BasketCmd, CatalogCmd, DoOrderCmd, FolderCmd,
        IndexCmd, InstructionCmd, ProductAboutCmd, QuantityMinusCmd, QuantityPlusCmd, StartCmd {

    User getUser();
}
