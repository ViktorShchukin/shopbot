package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;

public sealed interface Form permits AboutForm, BasketForm, CatalogForm, ContactForm, DistributionModeForm, ErrorForm, ForWholesalerForm, GuideForm, IndexForm, InvalidInputForLongForm, OrderAdditionalInfoAddressForm, OrderAdditionalInfoPhoneForm, OrderAdditionalInfoPhoneInvalidForm, OrderForm, PayAndDeliveryForm, PoolDepthForm, PoolDiameterForm, PoolLenghtForm, PoolSizeInfoForm, PoolTypeForm, PoolWidthForm, ProductAboutForm, ProductInstructionForm, ShopForm {

    void draw(View view);
}
