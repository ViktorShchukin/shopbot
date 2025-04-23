package ru.aquamarina.model.command;

import ru.aquamarina.model.entity.User;

public record AddProductToBasket(User user, String productName) implements Command {

    public static final String NAME = "addProductToBasket";
    @Override
    public User getUser() {
        return user;
    }
}
