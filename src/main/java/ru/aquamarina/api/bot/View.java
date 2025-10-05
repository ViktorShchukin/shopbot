package ru.aquamarina.api.bot;

import ru.aquamarina.fsm.form.*;
import ru.aquamarina.model.error.Error;

public interface View {

    void drawAboutForm(AboutForm form);

    void drawIndexForm(IndexForm form);

    void draw(Error error);

    void drawCatalogForm(CatalogForm form);

    void drawProductAboutForm(ProductAboutForm form);

    void drawOrderForm(OrderForm form);

    void drawBasketForm(BasketForm form);

    void drawForWholesalerForm(ForWholesalerForm form);

    void drawPayAndDeliveryFormForm(PayAndDeliveryForm form);

    void drawProductInstructionForm(ProductInstructionForm form);

    void drawErrorForm(ErrorForm form);

    void drawDistributionModeForm(DistributionModeForm form);

    void drawOrderAdditionalInfoAddressForm(OrderAdditionalInfoAddressForm form);

    void drawOrderAdditionalInfoPhoneForm(OrderAdditionalInfoPhoneForm form);

    @Deprecated
    void OrderAdditionalInfoPhoneInvalidForm(OrderAdditionalInfoPhoneInvalidForm form);

    void drawContactFrom(ContactForm form);
}
