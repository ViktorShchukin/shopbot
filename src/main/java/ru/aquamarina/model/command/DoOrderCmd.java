package ru.aquamarina.model.command;

import ru.aquamarina.model.entity.User;

public record DoOrderCmd(User user) implements Command {

    public static final String NAME = "doOrder";

    @Override
    public User getUser() {
        return user;
    }
}
