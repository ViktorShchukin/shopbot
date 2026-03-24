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

    @MappedProperty("first_name")
    private String firstName;

    @MappedProperty("last_name")
    private String lastName;

    @MappedProperty("user_name")
    private String userName;

    @MappedProperty("updated")
    private Boolean isUpdated;

    @MappedProperty("last_message_id")
    private Integer lastMessageId;

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getUpdated() {
        return isUpdated;
    }

    public void setUpdated(Boolean updated) {
        isUpdated = updated;
    }

    public Integer getLastMessageId() {
        return lastMessageId;
    }

    public void setLastMessageId(Integer lastMessageId) {
        this.lastMessageId = lastMessageId;
    }
}
