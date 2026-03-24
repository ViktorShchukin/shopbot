package ru.aquamarina.model.command;

import ru.aquamarina.model.entity.User;

public record BackCmd(User user) implements Command {
    public final static String NAME = "back";

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return NAME;
    }
}
