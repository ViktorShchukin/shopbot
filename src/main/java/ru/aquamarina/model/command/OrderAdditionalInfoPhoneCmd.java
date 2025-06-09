package ru.aquamarina.model.command;

import ru.aquamarina.model.entity.User;

public record OrderAdditionalInfoPhoneCmd(User user, String phoneNumber) implements Command {

    public final static String NAME = "+7";

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return NAME;
    }
}
