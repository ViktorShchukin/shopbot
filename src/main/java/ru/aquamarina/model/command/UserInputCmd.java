package ru.aquamarina.model.command;

import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.validation.StringParseError;
import ru.aquamarina.util.Result;

public record UserInputCmd(User user, String input) implements Command {

    public static final String NAME = "userInput";

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return new StringBuilder(NAME).append("?").append(input).toString();
    }

    public Result<Long, Error> asLong() {
        try {
            return Result.ok(Long.valueOf(input));
        } catch (NumberFormatException e) {
            return Result.error(new StringParseError(e.getMessage()));
        }

    }
}
