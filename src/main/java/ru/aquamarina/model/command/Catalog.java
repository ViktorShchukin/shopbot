package ru.aquamarina.model.command;

import ru.aquamarina.model.entity.User;

public record Catalog(User user) implements Command {

    public static final String NAME = "catalog";

    @Override
    public User getUser() {
        return user;
    }
}
