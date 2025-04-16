package ru.aquamarina.model.command;

import ru.aquamarina.model.entity.User;

public record Start(User user) implements Command {

    public static final String NAME = "/start";

    @Override
    public User getUser() {
        return user;
    }
}
