package ru.aquamarina.service;

import io.micronaut.data.exceptions.DataAccessException;
import jakarta.inject.Singleton;
import ru.aquamarina.fsm.state.FsmState;
import ru.aquamarina.guide.FilterType;
import ru.aquamarina.model.entity.PoolInfo;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.IoError;
import ru.aquamarina.util.Result;

import java.util.UUID;

@Singleton
public class PoolInfoService {

    private final PoolInfoServiceWithExc poolInfoService;

    public PoolInfoService(PoolInfoServiceWithExc poolInfoService) {
        this.poolInfoService = poolInfoService;
    }

    public Result<PoolInfo, Error> create(PoolInfo poolInfo) {
        try {
            return poolInfoService.create(poolInfo);
        } catch (DataAccessException e) {
            return Result.error(new IoError(e));
        }
    }

    public Result<PoolInfo, Error> update(
            UUID userId,
            Double poolDepth,
            Double poolLength,
            Double poolWidth,
            Double poolDiameter,
            Double poolVolume
    ) {
        try {
            var res = poolInfoService.update(
                    userId,
                    poolDepth,
                    poolLength,
                    poolWidth,
                    poolDiameter,
                    poolVolume
            );
            return res;
        } catch (DataAccessException e) {
            return Result.error(new IoError(e));
        }
    }

    public Result<PoolInfo, Error> createOrUpdate(PoolInfo info) {
        try {
            return poolInfoService.createOrUpdate(info);
        } catch (DataAccessException e) {
            return Result.error(new IoError(e));
        }
    }

    public Result<PoolInfo, Error> getPoolInfoByUserId(UUID userId) {
        try{
            return poolInfoService.getPoolInfoByUserId(userId);
        } catch (DataAccessException e){
            return Result.error(new IoError(e));
        }
    }

    public Result<PoolInfo, Error> updateFilterType(UUID userId,FilterType filterType) {
        try {
            return poolInfoService.update(
                    userId,
                    filterType,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
        } catch (DataAccessException e){
            return Result.error(new IoError(e));
        }

    }
}
