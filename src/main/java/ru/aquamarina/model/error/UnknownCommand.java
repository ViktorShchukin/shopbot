package ru.aquamarina.model.error;

import ru.aquamarina.model.entity.User;

public record UnknownCommand(User user) implements Error {
}
