package ru.aquamarina.guide.dto;

public record PoolGuideDto(
        Double poolVolume,
        Long phMinus,
        Long phPlus,
        Long algicide,
        Long chlorineGran,
        Long chlorinePill,
        Long slowChlorineSmallPill,
        Long slowChlorineBigPill,
        Long coagulatLiquid,
        Long coagulatPill
) {
}
