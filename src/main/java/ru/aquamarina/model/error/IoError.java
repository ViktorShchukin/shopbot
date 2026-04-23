package ru.aquamarina.model.error;

public record IoError(Exception e) implements Error {

    @Override
    public String toString() {
        e.printStackTrace();
        return "IoError: " + e.toString();
    }
}
