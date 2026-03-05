package ru.aquamarina.guide;

import ru.aquamarina.guide.dto.PoolGuideDto;
import ru.aquamarina.model.entity.PoolInfo;
import ru.aquamarina.util.MathUtil;

public class PoolGuideCalculator {

    public static final Long PH_MINUS_02_PER_CUBIC_METER = 15L;
    public static final Long PH_PLUS_02_PER_CUBIC_METER = 20L;
    public static final Long PH_ROUND_PRECISION = 10L;

    public static final Long ALGICIDE_PER_CUBIC_METER = 10L;
    public static final Long ALGICIDE_ROUND_PREDISION = 10L;

    public static final Long CHLORINE_GRAN_PER_CUBIC_METER = 10L;
    public static final Long CHLORINE_GRAN_ROUND_PRECISION = 50L;

    public static final Double CHLORINE_PILL_PER_CUBIC_METER = 0.5;
    public static final Long CHLORINE_PILL_ROUND_PRECISION = 1L;

    /**
     * in m^3
     */
    private final Double poolVolume;

    public PoolGuideCalculator(Double poolVolume) {
        this.poolVolume = poolVolume;
    }

    public static PoolGuideCalculator of(PoolInfo poolInfo) {
        Double res = switch (poolInfo.getPoolType()) {
            case CIRCLE -> evaluateCircleVolume(poolInfo);
            case RECTANGLE -> evaluateRectangle(poolInfo);
        };

        return new PoolGuideCalculator(res);
    }

    public PoolGuideDto evaluate() {
        return new PoolGuideDto(
                poolVolume,
                getPhMinusAmount(),
                getPhPlusAmount(),
                getAlgicideAmount(),
                getClorineGran(),
                getChlorinePill()
        );
    }

    public Long getChlorinePill() {
        return MathUtil.round(
                poolVolume * CHLORINE_PILL_PER_CUBIC_METER,
                CHLORINE_PILL_ROUND_PRECISION
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

    private static Double evaluateCircleVolume(PoolInfo poolInfo) {
        return poolInfo.getPoolDepth() * Math.PI * poolInfo.getPoolDiameter() * poolInfo.getPoolDiameter() / 4;
    }

    private static Double evaluateRectangle(PoolInfo poolInfo) {
        return (double) (poolInfo.getPoolDepth() * poolInfo.getPoolLength() * poolInfo.getPoolWidth());
    }
}
