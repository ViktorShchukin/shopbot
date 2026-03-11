package ru.aquamarina.model.command;

import ru.aquamarina.guide.FilterType;
import ru.aquamarina.model.entity.User;

public record FilterTypeCmd(User user, FilterType filterType) implements Command {
    public final static String NAME = "filterType";

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return NAME + "?" + filterType.name();
    }
}
