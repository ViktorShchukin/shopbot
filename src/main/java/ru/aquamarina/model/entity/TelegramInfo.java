package ru.aquamarina.model.entity;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;

import java.util.UUID;

@MappedEntity(value = "user_telegram_info")
public class TelegramInfo {

    @Id
    @MappedProperty(value = "telegram_id")
    private Long telegramId;

    @MappedProperty(value = "user_id")
    private UUID userId;

    public Long getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(Long telegramId) {
        this.telegramId = telegramId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
