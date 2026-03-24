package ru.aquamarina.model.command;

import ru.aquamarina.guide.GuideType;
import ru.aquamarina.model.entity.User;

public record GuideCmd(User user, GuideType guideType) implements Command {
    public static final String NAME = "guide";

    @Override
    public User getUser() {
        return user;
    }

    // todo think about using something else. It is not clear that you should override this method. And you can miss it.
    @Override
    public String toString() {
        return NAME + "?" + guideType.name();
    }
}
