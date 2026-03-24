package ru.aquamarina.model.entity;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;
import ru.aquamarina.model.DistributionMode;

import java.util.UUID;

@MappedEntity("user_order")
public class Order {

    @Id
    private UUID id;

    @MappedProperty("user_id")
    private UUID userId;

    @MappedProperty("phone_number")
    private String phoneNumber;

    @MappedProperty("address")
    private String address;

    @MappedProperty
    private DistributionMode distributionMode;

    @MappedProperty("additional_info")
    private String additionalInfo;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public DistributionMode getDistributionMode() {
        return distributionMode;
    }

    public void setDistributionMode(DistributionMode distributionMode) {
        this.distributionMode = distributionMode;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}
