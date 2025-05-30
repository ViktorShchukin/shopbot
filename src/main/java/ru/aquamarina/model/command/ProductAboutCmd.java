package ru.aquamarina.model.command;

import ru.aquamarina.model.entity.User;

import java.util.UUID;

public record ProductAboutCmd(User user, UUID productId) implements Command {

    // todo think how to pass product name
    public static final String NAME = "productAbout";

    @Override
    public User getUser() {
        return user;
    }

    // todo think about using something else. It is not clear that you should override this method. And you can miss it.
    @Override
    public String toString() {
        return new StringBuilder(NAME).append("?").append(productId.toString()).toString();
    }
}
