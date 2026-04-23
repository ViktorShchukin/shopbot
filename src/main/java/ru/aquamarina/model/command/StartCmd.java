package ru.aquamarina.model.command;

import ru.aquamarina.model.entity.User;

import java.util.Optional;

public record StartCmd(User user, String source) implements Command {

    public static final String NAME = "/start";

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return NAME;
    }

    public Optional<String> getSource() {
        return Optional.ofNullable(source);
    }
}
