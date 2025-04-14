package ru.aquamarina.model.command;

import java.util.UUID;

public record About(UUID userId) implements Command {

    static String NAME = "about";

    @Override
    public UUID getUserId() {
        return userId;
    }

    public static String getCommandName() {
        return NAME;
    }
}
