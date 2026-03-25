package ru.aquamarina.guide;

import ru.aquamarina.guide.dto.PoolInfoDto;
import ru.aquamarina.model.entity.PoolInfo;

public enum GuideType {
    GREEN_POOL {
        @Override
        public IPoolGuideCalculator getCalculator(PoolInfoDto poolInfo) {
            return PoolGuideCalculatorBeginning.of(poolInfo);
        }

        @Override
        public String getTemplate() {
            return "static/templates/green-pool.html";
        }
    },
    //Запуск бассейна в начале сезона
    BEGINNING_OF_SEASON {
        @Override
        public IPoolGuideCalculator getCalculator(PoolInfoDto poolInfo) {
            return PoolGuideCalculatorBeginning.of(poolInfo);
        }

        @Override
        public String getTemplate() {
            return "static/templates/guide.html";
        }
    },

    //Уход за бассейном. Пошаговая инструкция.
    STEP_BY_STEP {
        @Override
        public IPoolGuideCalculator getCalculator(PoolInfoDto poolInfo) {
            return PoolGuideCalculatorDefault.of(poolInfo);
        }

        @Override
        public String getTemplate() {
            return "static/templates/guide.html";
        }
    };



    public abstract IPoolGuideCalculator getCalculator(PoolInfoDto poolInfo);
    public abstract String getTemplate();

}
