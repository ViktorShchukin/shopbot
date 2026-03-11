package ru.aquamarina.service;

import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import ru.aquamarina.guide.FilterType;
import ru.aquamarina.guide.PoolType;
import ru.aquamarina.mapper.PoolInfoTool;
import ru.aquamarina.model.entity.PoolInfo;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotFound;
import ru.aquamarina.repository.PollInfoRepository;
import ru.aquamarina.util.Result;

import java.util.UUID;

@Singleton
@Transactional
public class PoolInfoServiceWithExc {

    private final PollInfoRepository pollInfoRepository;
    private final PoolInfoTool poolInfoTool;

    public PoolInfoServiceWithExc(
            PollInfoRepository pollInfoRepository,
            PoolInfoTool poolInfoTool) {
        this.pollInfoRepository = pollInfoRepository;
        this.poolInfoTool = poolInfoTool;
    }

    public Result<PoolInfo, Error> update(
            UUID userId,
            Double poolDepth,
            Double poolLength,
            Double poolWidth,
            Double poolDiameter,
            Double poolVolume
    ) {
        return update(
                userId,
                null,
                poolDepth,
                poolLength,
                poolWidth,
                poolDiameter,
                poolVolume
        );
    }

    public Result<PoolInfo, Error> update(
            UUID userId,
            PoolType poolType,
            Double poolDepth,
            Double poolLength,
            Double poolWidth,
            Double poolDiameter,
            Double poolVolume
    ) {
        return update(
                userId,
                null,
                poolType,
                poolDepth,
                poolLength,
                poolWidth,
                poolDiameter,
                poolVolume
        );
    }

    public Result<PoolInfo, Error> update(
            UUID userId,
            FilterType filterType,
            PoolType poolType,
            Double poolDepth,
            Double poolLength,
            Double poolWidth,
            Double poolDiameter,
            Double poolVolume
    ) {
        return getPoolInfoByUserId(userId)
                .mapValue(value ->
                        poolInfoTool.update(
                                value,
                                filterType,
                                poolType,
                                poolDepth,
                                poolLength,
                                poolWidth,
                                poolDiameter,
                                poolVolume
                        )
                )
                .mapValue(pollInfoRepository::update);
    }

    public Result<PoolInfo, Error> create(PoolInfo poolInfo) {
        return Result.ok(
                pollInfoRepository.save(poolInfo)
        );
    }

    public Result<PoolInfo, Error> createOrUpdate(PoolInfo info) {
        return update(info.getUserId(), info.getPoolType(), 0.0, 0.0, 0.0, 0.0, 0.0)
                .or(error -> {
                    if(error instanceof NotFound){
                        return create(info);
                    } else {
                        return Result.error(error);
                    }
                });
    }

    public Result<PoolInfo, Error> getPoolInfoByUserId(UUID userId) {
        return pollInfoRepository.findByUserId(userId)
                .map(Result::<PoolInfo, Error>ok)
                .orElseGet(() -> Result.error(new NotFound("this poolInfo not found")));
    }
}
