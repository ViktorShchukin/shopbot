package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;

public sealed interface Form permits AboutForm, BasketForm, CatalogForm, ContactForm, DistributionModeForm, ErrorForm, ForWholesalerForm, GuideForm, IndexForm, OrderAdditionalInfoAddressForm, OrderAdditionalInfoPhoneForm, OrderAdditionalInfoPhoneInvalidForm, OrderForm, PayAndDeliveryForm, PoolSizeInfoForm, PoolTypeForm, ProductAboutForm, ProductInstructionForm, ShopForm {

    void draw(View view);
}
