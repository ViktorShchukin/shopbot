package ru.aquamarina.model.error;

public record InvalidPhoneNumber(String phoneNumber) implements Error {
}
