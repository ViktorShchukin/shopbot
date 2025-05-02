package ru.aquamarina.model.command;

import ru.aquamarina.model.entity.User;

public record Basket(User user) implements Command {

    public static final String NAME = "basket";

    @Override
    public User getUser() {
        return user;
    }
}
