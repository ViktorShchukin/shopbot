package ru.aquamarina.guide;

import ru.aquamarina.model.entity.PoolInfo;

public enum GuideType {
    //Запуск бассейна в начале сезона
    BEGINNING_OF_SEASON {
        @Override
        public IPoolGuideCalculator getCalculator(PoolInfo poolInfo) {
            return PoolGuideCalculatorBeginning.of(poolInfo);
        }
    },

    //Уход за бассейном. Пошаговая инструкция.
    STEP_BY_STEP {
        @Override
        public IPoolGuideCalculator getCalculator(PoolInfo poolInfo) {
            return PoolGuideCalculatorDefault.of(poolInfo);
        }
    };

    public abstract IPoolGuideCalculator getCalculator(PoolInfo poolInfo);

}
