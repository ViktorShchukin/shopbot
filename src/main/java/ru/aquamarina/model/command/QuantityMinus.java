package ru.aquamarina.model.command;

import ru.aquamarina.model.entity.User;

public record QuantityMinus(User user) implements Command {

    public static final String NAME = "quantityMinus";

    @Override
    public User getUser() {
        return user;
    }
}
