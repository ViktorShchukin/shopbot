package ru.aquamarina.model.command;

import ru.aquamarina.model.entity.User;

public record FolderCmd(User user, String path) implements Command {

    public static final String NAME = "catalog";

    @Override
    public User getUser() {
        return user;
    }
}
