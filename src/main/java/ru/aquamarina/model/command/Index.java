package ru.aquamarina.model.command;

import java.util.UUID;

public record Index(UUID userId) implements Command {

    public static final String NAME = "index";

    @Override
    public UUID getUserId() {
        return userId;
    }
}
