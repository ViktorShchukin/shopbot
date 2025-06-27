package ru.aquamarina.model.command;

import ru.aquamarina.model.entity.User;

public sealed interface Command
        permits AboutCmd, AddToBasketCmd, BasketCmd, CatalogCmd, ClearBasketCmd, DeliveryCmd, DoNothing, DoOrderCmd, FolderCmd, ForWholesalerCmd, IndexCmd, InstructionCmd, OrderAdditionalInfoAddressCmd, OrderAdditionalInfoPhoneCmd, PayAndDeliveryCmd, ProductAboutCmd, QuantityMinusCmd, QuantityPlusCmd, SelfPickupCmd, StartCmd, UserInputCmd {

    User getUser();
}
