package ru.aquamarina.model.command;

import ru.aquamarina.model.entity.User;

public record AddToBasketCmd(User user) implements Command {

    public static final String NAME = "addToBasket";
    @Override
    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return NAME;
    }
}
