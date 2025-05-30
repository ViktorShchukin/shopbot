package ru.aquamarina.model.error;

public record ExceptionWrapperError(Exception exception, String message) implements Error {
}
