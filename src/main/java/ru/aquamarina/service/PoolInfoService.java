package ru.aquamarina.service;

import io.micronaut.data.exceptions.DataAccessException;
import jakarta.inject.Singleton;
import ru.aquamarina.instruction.PoolType;
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
            Long poolDepth,
            Long poolLength,
            Long poolWidth,
            Long poolDiameter,
            Long poolVolume
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
}
