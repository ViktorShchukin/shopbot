package ru.aquamarina.model.command;

import java.util.UUID;

public record Catalog(UUID userId) implements Command {

    public static final String NAME = "catalog";

    @Override
    public UUID getUserId() {
        return userId;
    }
}
