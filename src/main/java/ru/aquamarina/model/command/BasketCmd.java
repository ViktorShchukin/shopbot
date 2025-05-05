package ru.aquamarina.model.command;

import ru.aquamarina.model.entity.User;

public record BasketCmd(User user) implements Command {

    public static final String NAME = "basket";

    @Override
    public User getUser() {
        return user;
    }
}
