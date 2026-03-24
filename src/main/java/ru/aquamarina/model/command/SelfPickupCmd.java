package ru.aquamarina.model.command;

import ru.aquamarina.model.entity.User;

public record SelfPickupCmd(User user) implements Command {

    public final static String NAME = "selfPickup";

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return NAME;
    }
}
