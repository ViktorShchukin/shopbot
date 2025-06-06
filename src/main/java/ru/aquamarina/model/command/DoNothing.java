package ru.aquamarina.model.command;

import ru.aquamarina.model.entity.User;

public record DoNothing(User user) implements Command {

    public static final String NAME = "doNothing";

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return NAME;
    }
}
