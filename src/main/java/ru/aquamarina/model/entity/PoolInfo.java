package ru.aquamarina.model.entity;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import ru.aquamarina.instruction.PoolType;

import java.util.UUID;

@MappedEntity(value = "pool_info")
public class PoolInfo {
    @Id
    private UUID id;
    private UUID userId;
    private PoolType poolType;
    private Long poolDepth;
    private Long poolLength;
    private Long poolWidth;
    private Long poolDiameter;
    private Long poolVolume;

    public PoolInfo(UUID id, UUID userId, PoolType poolType, Long poolDepth, Long poolLength, Long poolWidth, Long poolDiameter, Long poolVolume) {
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
                0L,
                0L,
                0L,
                0L,
                0L
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

    public Long getPoolDepth() {
        return poolDepth;
    }

    public void setPoolDepth(Long poolDepth) {
        this.poolDepth = poolDepth;
    }

    public Long getPoolLength() {
        return poolLength;
    }

    public void setPoolLength(Long poolLength) {
        this.poolLength = poolLength;
    }

    public Long getPoolWidth() {
        return poolWidth;
    }

    public void setPoolWidth(Long poolWidth) {
        this.poolWidth = poolWidth;
    }

    public Long getPoolDiameter() {
        return poolDiameter;
    }

    public void setPoolDiameter(Long poolDiameter) {
        this.poolDiameter = poolDiameter;
    }

    public Long getPoolVolume() {
        return poolVolume;
    }

    public void setPoolVolume(Long poolVolume) {
        this.poolVolume = poolVolume;
    }
}
