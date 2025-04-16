package ru.aquamarina.model.command;

import ru.aquamarina.model.entity.User;

public record Index(User user) implements Command {

    public static final String NAME = "index";

    @Override
    public User getUser() {
        return user;
    }
}
