package ru.aquamarina.model.command;

import ru.aquamarina.model.entity.User;

@Deprecated
public record OrderAdditionalInfoAddressCmd(User user, String address) implements Command {

    public final static String NAME = "г.";

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return NAME;
    }
}
