package ru.aquamarina.guide.dto;

import io.micronaut.serde.annotation.Serdeable;
import ru.aquamarina.guide.FilterType;
import ru.aquamarina.guide.PoolType;

@Serdeable
public class PoolInfoDto {

    private PoolType poolType;
    private FilterType filterType;
    private Double poolDepth;
    private Double poolLength;
    private Double poolWidth;
    private Double poolDiameter;
    private Double poolVolume;

    public PoolInfoDto(
            PoolType poolType,
            FilterType filterType,
            Double poolDepth,
            Double poolLength,
            Double poolWidth,
            Double poolDiameter,
            Double poolVolume
    ) {
        this.poolType = poolType;
        this.filterType = filterType;
        this.poolDepth = poolDepth;
        this.poolLength = poolLength;
        this.poolWidth = poolWidth;
        this.poolDiameter = poolDiameter;
        this.poolVolume = poolVolume;
    }

    public FilterType getFilterType() {
        return filterType;
    }

    public void setFilterType(FilterType filterType) {
        this.filterType = filterType;
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