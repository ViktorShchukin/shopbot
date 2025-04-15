package ru.aquamarina.model.entity;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

import java.util.UUID;

@MappedEntity
public class TelegramInfo {

    @Id
    private Long telegram_id;
    private UUID user_id;

    public Long getTelegram_id() {
        return telegram_id;
    }

    public void setTelegram_id(Long telegram_id) {
        this.telegram_id = telegram_id;
    }

    public UUID getUser_id() {
        return user_id;
    }

    public void setUser_id(UUID user_id) {
        this.user_id = user_id;
    }
}
