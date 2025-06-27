package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;

public sealed interface Form permits AboutForm, BasketForm, CatalogForm, DistributionModeForm, ErrorForm, ForWholesalerForm, IndexForm, OrderAdditionalInfoAddressForm, OrderAdditionalInfoPhoneForm, OrderAdditionalInfoPhoneInvalidForm, OrderForm, PayAndDeliveryForm, ProductAboutForm, ProductInstructionForm {

    void draw(View view);
}
