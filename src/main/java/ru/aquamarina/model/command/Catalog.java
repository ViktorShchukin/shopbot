package ru.aquamarina.model.command;

import java.util.UUID;

public record Catalog(UUID userId) implements Command {

    static String NAME = "catalog";

    @Override
    public UUID getUserId() {
        return userId;
    }

    public static String getCommandName() {
        return NAME;
    }
}
