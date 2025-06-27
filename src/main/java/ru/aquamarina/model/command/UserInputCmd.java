package ru.aquamarina.model.command;

import ru.aquamarina.model.entity.User;

public record UserInputCmd(User user, String input) implements Command {

    public static final String NAME = "userInput";

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return new StringBuilder(NAME).append("?").append(input).toString();
    }
}
