package ru.aquamarina.model.command;

import java.util.UUID;

public record ProductAbout(UUID userId) implements Command {

    // todo think how to pass product name
    static String NAME = "productAbout";

    @Override
    public UUID getUserId() {
        return userId;
    }

    public static String getCommandName() {
        return NAME;
    }
}
