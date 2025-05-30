package ru.aquamarina.model.command;

import ru.aquamarina.model.entity.User;

public record FolderCmd(User user, String path) implements Command {

    public static final String NAME = "f";

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return new StringBuilder(NAME).append("?").append(path).toString();
    }
}
