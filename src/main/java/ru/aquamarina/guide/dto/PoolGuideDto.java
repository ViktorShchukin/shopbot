package ru.aquamarina.guide.dto;

import ru.aquamarina.guide.FilterType;
import ru.aquamarina.guide.PoolType;

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
        Long coagulatPill,
        FilterType filterType
) {
}
