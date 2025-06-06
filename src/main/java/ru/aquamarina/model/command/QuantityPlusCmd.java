package ru.aquamarina.model.command;

import ru.aquamarina.model.entity.Product;
import ru.aquamarina.model.entity.User;

import java.util.UUID;

public record QuantityPlusCmd(User user, UUID productId) implements Command {

    public static final String NAME = "quantityPlus";

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return NAME + "?" + productId;
    }
}
