package ru.aquamarina.model.command;

import ru.aquamarina.model.entity.User;

public record CatalogCmd(User user) implements Command {

    public static final String NAME = "catalog";

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return NAME;
    }
}
