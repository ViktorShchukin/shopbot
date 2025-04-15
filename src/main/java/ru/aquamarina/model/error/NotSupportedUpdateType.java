package ru.aquamarina.model.error;

import org.telegram.telegrambots.meta.api.objects.Update;

public record NotSupportedUpdateType(Update update) implements Error {
}
