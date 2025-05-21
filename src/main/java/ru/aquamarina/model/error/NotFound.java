package ru.aquamarina.model.error;

public record NotFound(String message) implements Error {

    @Override
    public String toString() {
        return message;
    }
}
