package ru.aquamarina.guide;

import ru.aquamarina.guide.dto.PoolGuideDto;
import ru.aquamarina.guide.dto.PoolInfoDto;
import ru.aquamarina.util.MathUtil;

public class PoolGuideCalculatorBeginning implements IPoolGuideCalculator {

    public static Long ALGICIDE_PER_CUBIC_METER_BEGINNING = 20L;
    public static Long CHLORINE_GRAN_PER_CUBIC_METER_BEGINNING = 20L;
    public static Long CHLORINE_PILL_PER_CUBIC_METER_BEGINNING = 1L;

    /**
     * in m^3
     */
    private final Double poolVolume;
    private final PoolInfoDto poolInfo;

    public PoolGuideCalculatorBeginning(Double poolVolume, PoolInfoDto poolInfo) {
        this.poolVolume = poolVolume;
        this.poolInfo = poolInfo;
    }

    public static PoolGuideCalculatorBeginning of(PoolInfoDto poolInfo) {
        Double res = switch (poolInfo.getPoolType()) {
            case CIRCLE -> IPoolGuideCalculator.evaluateCircleVolume(poolInfo);
            case RECTANGLE -> IPoolGuideCalculator.evaluateRectangleVolume(poolInfo);
        };
        res *= 0.85; // coefficient for pool size. see doc/meeting/2026-03-12-meet-summary.md  7th point.
        return new PoolGuideCalculatorBeginning(res, poolInfo);
    }

    @Override
    public PoolGuideDto evaluate() {
        return new PoolGuideDto(
                poolVolume,
                getPhMinusAmount(),
                getPhPlusAmount(),
                getAlgicideAmount(),
                getChlorineGran(),
                getChlorinePill(),
                getSlowChlorineSmall(),
                getSlowChlorineBig(),
                getCoagulatLiquid(),
                getCoagulatPill(),
                poolInfo.getFilterType()
        );
    }

    @Override
    public Long getCoagulatLiquid() {
        return MathUtil.round(
                poolVolume * COAGULAT_LIQUID_PER_CUBIC_METER,
                COAGULAT_LIQUID_ROUND_PRECISION
        );
    }

    @Override
    public Long getCoagulatPill() {
        return MathUtil.round(
                poolVolume * COAGULAT_PILL_PER_CUBIC_METER,
                PILL_ROUND_PRECISION
        );
    }

    @Override
    public Long getSlowChlorineBig() {
        return MathUtil.round(
                poolVolume * SLOW_CHLORINE_BIG_PER_CUBIC_METER,
                PILL_ROUND_PRECISION
        );
    }

    @Override
    public Long getSlowChlorineSmall() {
        return MathUtil.round(
                poolVolume * SLOW_CHLORINE_SMALL_PER_CUBIC_METER,
                PILL_ROUND_PRECISION
        );
    }

    @Override
    public Long getChlorinePill() {
        return MathUtil.round(
                poolVolume * CHLORINE_PILL_PER_CUBIC_METER_BEGINNING,
                PILL_ROUND_PRECISION
        );
    }

    @Override
    public Long getChlorineGran() {
        return MathUtil.round(
                poolVolume * CHLORINE_GRAN_PER_CUBIC_METER_BEGINNING,
                CHLORINE_GRAN_ROUND_PRECISION
        );
    }

    @Override
    public Long getAlgicideAmount() {
        return MathUtil.round(
                poolVolume * ALGICIDE_PER_CUBIC_METER_BEGINNING,
                ALGICIDE_ROUND_PREDISION
        );
    }

    @Override
    public Long getPhMinusAmount() {
        return MathUtil.round(
                poolVolume * PH_MINUS_02_PER_CUBIC_METER,
                PH_ROUND_PRECISION
        );
    }

    @Override
    public Long getPhPlusAmount() {
        return MathUtil.round(
                poolVolume * PH_PLUS_02_PER_CUBIC_METER,
                PH_ROUND_PRECISION
        );
    }

    @Override
    public Double getPoolVolume() {
        return poolVolume;
    }

}
