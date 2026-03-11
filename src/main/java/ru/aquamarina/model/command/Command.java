package ru.aquamarina.model.command;

import ru.aquamarina.model.entity.User;

public sealed interface Command
        permits AboutCmd, AddToBasketCmd, BasketCmd, CatalogCmd, CircleCmd, ClearBasketCmd, ContactCmd, DeliveryCmd, DoNothing, DoOrderCmd, FilterTypeCmd, FolderCmd, ForWholesalerCmd, GuideCmd, GuideTypeCmd, IndexCmd, InstructionCmd, OrderAdditionalInfoAddressCmd, OrderAdditionalInfoPhoneCmd, PayAndDeliveryCmd, PoolTypeCmd, ProductAboutCmd, QuantityMinusCmd, QuantityPlusCmd, RectangleCmd, SelfPickupCmd, ShopCmd, StartCmd, UserInputCmd {

    User getUser();
}
