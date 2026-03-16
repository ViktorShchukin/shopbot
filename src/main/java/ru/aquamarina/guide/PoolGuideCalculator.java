package ru.aquamarina.guide;

import ru.aquamarina.guide.dto.PoolGuideDto;
import ru.aquamarina.model.entity.PoolInfo;
import ru.aquamarina.util.MathUtil;

public class PoolGuideCalculator {

    ////// all constants are count in grams, milliliters and pills
    public static final Long PH_MINUS_02_PER_CUBIC_METER = 15L;
    public static final Long PH_PLUS_02_PER_CUBIC_METER = 20L;
    public static final Long PH_ROUND_PRECISION = 10L;

    public static final Long ALGICIDE_PER_CUBIC_METER = 10L;
    public static final Long ALGICIDE_ROUND_PREDISION = 10L;

    public static final Long CHLORINE_GRAN_PER_CUBIC_METER = 10L;
    public static final Long CHLORINE_GRAN_ROUND_PRECISION = 10L;

    public static final Double CHLORINE_PILL_PER_CUBIC_METER = 0.5;
    public static final Long PILL_ROUND_PRECISION = 1L;

    public static final Double SLOW_CHLORINE_SMALL_PER_CUBIC_METER = 0.5;
    public static final Double SLOW_CHLORINE_BIG_PER_CUBIC_METER = 0.05;

    public static final Long COAGULAT_LIQUID_PER_CUBIC_METER = 5L;
    public static final Double COAGULAT_PILL_PER_CUBIC_METER = 0.25;
    public static final Long COAGULAT_LIQUID_ROUND_PRECISION = 10L;

    /**
     * in m^3
     */
    private final Double poolVolume;
    private final PoolInfo poolInfo;

    public PoolGuideCalculator(Double poolVolume, PoolInfo poolInfo) {
        this.poolVolume = poolVolume;
        this.poolInfo = poolInfo;
    }

    public static PoolGuideCalculator of(PoolInfo poolInfo) {
        Double res = switch (poolInfo.getPoolType()) {
            case CIRCLE -> evaluateCircleVolume(poolInfo);
            case RECTANGLE -> evaluateRectangle(poolInfo);
        };
        res *= 0.85; // coefficient for pool size. see doc/meeting/2026-03-12-meet-summary.md  7th point.
        return new PoolGuideCalculator(res, poolInfo);
    }

    public PoolGuideDto evaluate() {
        return new PoolGuideDto(
                poolVolume,
                getPhMinusAmount(),
                getPhPlusAmount(),
                getAlgicideAmount(),
                getClorineGran(),
                getChlorinePill(),
                getSlowChlorineSmall(),
                getSlowChlorineBig(),
                getCoagulatLiquid(),
                getCoagulatPill(),
                poolInfo.getFilterType()
        );
    }

    private Long getCoagulatLiquid() {
        return MathUtil.round(
                poolVolume * COAGULAT_LIQUID_PER_CUBIC_METER,
                COAGULAT_LIQUID_ROUND_PRECISION
        );
    }

    private Long getCoagulatPill() {
        return MathUtil.round(
                poolVolume * COAGULAT_PILL_PER_CUBIC_METER,
                PILL_ROUND_PRECISION
        );
    }

    public Long getSlowChlorineBig() {
        return MathUtil.round(
                poolVolume * SLOW_CHLORINE_BIG_PER_CUBIC_METER,
                PILL_ROUND_PRECISION
        );
    }

    public Long getSlowChlorineSmall() {
        return MathUtil.round(
                poolVolume * SLOW_CHLORINE_SMALL_PER_CUBIC_METER,
                PILL_ROUND_PRECISION
        );
    }

    public Long getChlorinePill() {
        return MathUtil.round(
                poolVolume * CHLORINE_PILL_PER_CUBIC_METER,
                PILL_ROUND_PRECISION
        );
    }

    public Long getClorineGran() {
        return MathUtil.round(
                poolVolume * CHLORINE_GRAN_PER_CUBIC_METER,
                CHLORINE_GRAN_ROUND_PRECISION
        );
    }

    public Long getAlgicideAmount() {
        return MathUtil.round(
                poolVolume * ALGICIDE_PER_CUBIC_METER,
                ALGICIDE_ROUND_PREDISION
        );
    }

    public Long getPhMinusAmount() {
        return MathUtil.round(
                poolVolume * PH_MINUS_02_PER_CUBIC_METER,
                PH_ROUND_PRECISION
        );
    }

    public Long getPhPlusAmount() {
        return MathUtil.round(
                poolVolume * PH_PLUS_02_PER_CUBIC_METER,
                PH_ROUND_PRECISION
        );
    }

    public Double getPoolVolume() {
        return poolVolume;
    }

    private static Double evaluateCircleVolume(PoolInfo poolInfo) {
        return poolInfo.getPoolDepth() * Math.PI * poolInfo.getPoolDiameter() * poolInfo.getPoolDiameter() / 4;
    }

    private static Double evaluateRectangle(PoolInfo poolInfo) {
        return (double) (poolInfo.getPoolDepth() * poolInfo.getPoolLength() * poolInfo.getPoolWidth());
    }
}
