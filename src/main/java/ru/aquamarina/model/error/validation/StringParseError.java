package ru.aquamarina.model.error.validation;

public record StringParseError(String message) implements ValidationError {
}
