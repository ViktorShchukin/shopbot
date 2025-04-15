package ru.aquamarina.model.command;

import org.checkerframework.checker.units.qual.N;

import java.util.UUID;

public record Start(UUID userId) implements Command {

    public static final String NAME = "/start";

    @Override
    public UUID getUserId() {
        return userId;
    }
}
