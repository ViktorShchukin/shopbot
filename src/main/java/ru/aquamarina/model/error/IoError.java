package ru.aquamarina.model.error;

public record IoError(Exception e) implements Error {
}
