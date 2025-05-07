package ru.aquamarina.model.entity;

import io.micronaut.core.annotation.Introspected;

import java.util.UUID;

@Introspected
public class OrderRow {

    private final UUID orderId;
    private final UUID productId;
    private final Long quantity;

    public OrderRow(UUID orderId, UUID productId, Long quantity) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
