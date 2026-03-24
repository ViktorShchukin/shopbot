package ru.aquamarina.model.command;

import ru.aquamarina.model.entity.User;

import java.util.UUID;

public record QuantityMinusCmd(User user, UUID productId) implements Command {

    public static final String NAME = "quantityMinus";

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return NAME + "?" + productId;
    }
}
