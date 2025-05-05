package ru.aquamarina.model.command;

import ru.aquamarina.model.entity.User;

public record InstructionCmd(User user) implements Command {

    public static final String NAME = "instruction";

    @Override
    public User getUser() {
        return null;
    }
}
