package ru.aquamarina.model.entity;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import ru.aquamarina.guide.PoolType;

import java.util.UUID;

@MappedEntity(value = "pool_info")
public class PoolInfo {
    @Id
    private UUID id;
    private UUID userId;
    private PoolType poolType;
    private Double poolDepth;
    private Double poolLength;
    private Double poolWidth;
    private Double poolDiameter;
    private Double poolVolume;

    public PoolInfo(UUID id, UUID userId, PoolType poolType, Double poolDepth, Double poolLength, Double poolWidth, Double poolDiameter, Double poolVolume) {
        this.id = id;
        this.userId = userId;
        this.poolType = poolType;
        this.poolDepth = poolDepth;
        this.poolLength = poolLength;
        this.poolWidth = poolWidth;
        this.poolDiameter = poolDiameter;
        this.poolVolume = poolVolume;
    }

    public static PoolInfo of(UUID id, UUID userId, PoolType poolType) {
        return new PoolInfo(
                id,
                userId,
                poolType,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0
        );
    }

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

    public PoolType getPoolType() {
        return poolType;
    }

    public void setPoolType(PoolType poolType) {
        this.poolType = poolType;
    }

    public Double getPoolDepth() {
        return poolDepth;
    }

    public void setPoolDepth(Double poolDepth) {
        this.poolDepth = poolDepth;
    }

    public Double getPoolLength() {
        return poolLength;
    }

    public void setPoolLength(Double poolLength) {
        this.poolLength = poolLength;
    }

    public Double getPoolWidth() {
        return poolWidth;
    }

    public void setPoolWidth(Double poolWidth) {
        this.poolWidth = poolWidth;
    }

    public Double getPoolDiameter() {
        return poolDiameter;
    }

    public void setPoolDiameter(Double poolDiameter) {
        this.poolDiameter = poolDiameter;
    }

    public Double getPoolVolume() {
        return poolVolume;
    }

    public void setPoolVolume(Double poolVolume) {
        this.poolVolume = poolVolume;
    }
}
