package ru.aquamarina.model.command;

import ru.aquamarina.model.entity.User;

public record CircleCmd(User user) implements Command {

    public final static String NAME = "circle";

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return NAME;
    }
}
