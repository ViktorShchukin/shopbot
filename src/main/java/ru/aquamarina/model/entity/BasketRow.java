package ru.aquamarina.model.entity;

import java.util.UUID;

// todo it may ge record. read about micronaut data and record compatibility
public class BasketRow {

    private final UUID basketId;
    private final UUID productId;
    private final Long quantity;

    public BasketRow(UUID userId, UUID productId, Long quantity) {
        this.basketId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public UUID getBasketId() {
        return basketId;
    }

    public UUID getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
