package ru.aquamarina.model.entity;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@MappedEntity("user_audit_log")
public class UserAuditEntry{

    @Id
    private UUID id;
    private Instant timestamp;
    private UUID userId;
    private String event;
    private String details;

    public UserAuditEntry(UUID id, Instant timestamp, UUID userId, String event, String details) {
        this.id = id;
        this.timestamp = timestamp;
        this.userId = userId;
        this.event = event;
        this.details = details;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
