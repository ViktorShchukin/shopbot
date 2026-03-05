package ru.aquamarina.guide;

import ru.aquamarina.guide.dto.PoolGuideDto;
import ru.aquamarina.model.entity.PoolInfo;
import ru.aquamarina.util.MathUtil;

public class PoolGuideCalculator {

    public static final Long PH_MINUS_02_PER_CUBIC_METER = 15L;
    public static final Long PH_MINUS_ROUND_PRECISION = 10L;

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
                getPhMinusAmount()
        );
    }

    public Long getPhMinusAmount() {
        return MathUtil.round(
                poolVolume * PH_MINUS_02_PER_CUBIC_METER,
                PH_MINUS_ROUND_PRECISION
        );
    }

    private static Double evaluateCircleVolume(PoolInfo poolInfo) {
        return poolInfo.getPoolDepth() * Math.PI * poolInfo.getPoolDiameter() * poolInfo.getPoolDiameter() / 4;
    }

    private static Double evaluateRectangle(PoolInfo poolInfo) {
        return (double) (poolInfo.getPoolDepth() * poolInfo.getPoolLength() * poolInfo.getPoolWidth());
    }
}
