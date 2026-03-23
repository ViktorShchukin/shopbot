package ru.aquamarina.guide;

import ru.aquamarina.guide.dto.PoolGuideDto;
import ru.aquamarina.model.entity.PoolInfo;

public interface IPoolGuideCalculator {
    ////// all constants are count in grams, milliliters and pills
    Long PH_MINUS_02_PER_CUBIC_METER = 15L;
    Long PH_PLUS_02_PER_CUBIC_METER = 20L;
    Long PH_ROUND_PRECISION = 10L;
    Long ALGICIDE_PER_CUBIC_METER = 10L;
    Long ALGICIDE_ROUND_PREDISION = 10L;
    Long CHLORINE_GRAN_PER_CUBIC_METER = 10L;
    Long CHLORINE_GRAN_ROUND_PRECISION = 10L;
    Double CHLORINE_PILL_PER_CUBIC_METER = 0.5;
    Long PILL_ROUND_PRECISION = 1L;
    Double SLOW_CHLORINE_SMALL_PER_CUBIC_METER = 0.5;
    Double SLOW_CHLORINE_BIG_PER_CUBIC_METER = 0.05;
    Long COAGULAT_LIQUID_PER_CUBIC_METER = 5L;
    Double COAGULAT_PILL_PER_CUBIC_METER = 0.25;
    Long COAGULAT_LIQUID_ROUND_PRECISION = 10L;

    static Double evaluateCircleVolume(PoolInfo poolInfo) {
        return poolInfo.getPoolDepth() * Math.PI * poolInfo.getPoolDiameter() * poolInfo.getPoolDiameter() / 4;
    }

    static Double evaluateRectangleVolume(PoolInfo poolInfo) {
        return (double) (poolInfo.getPoolDepth() * poolInfo.getPoolLength() * poolInfo.getPoolWidth());
    }

    PoolGuideDto evaluate();

    Long getCoagulatLiquid();

    Long getCoagulatPill();

    Long getSlowChlorineBig();

    Long getSlowChlorineSmall();

    Long getChlorinePill();

    Long getChlorineGran();

    Long getAlgicideAmount();

    Long getPhMinusAmount();

    Long getPhPlusAmount();

    Double getPoolVolume();
}
