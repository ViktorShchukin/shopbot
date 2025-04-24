package ru.aquamarina.model.command;

import ru.aquamarina.model.entity.User;

public record QuantityPlus(User user) implements Command {

    public static final String NAME = "quantityPlus";

    @Override
    public User getUser() {
        return user;
    }
}
