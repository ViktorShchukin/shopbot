package ru.aquamarina.model.command;

import ru.aquamarina.model.entity.User;

public record ProductAbout(User user, String productName) implements Command {

    // todo think how to pass product name
    public static final String NAME = "productAbout";

    @Override
    public User getUser() {
        return user;
    }
}
