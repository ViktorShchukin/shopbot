package ru.aquamarina.model.command;

import java.util.UUID;

public record Index(UUID userId) implements Command {

    static String NAME = "index";

    @Override
    public UUID getUserId() {
        return userId;
    }

    public static String getCommandName() {
        return NAME;
    }
}
