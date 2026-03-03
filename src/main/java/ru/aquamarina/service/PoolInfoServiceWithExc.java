package ru.aquamarina.service;

import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import ru.aquamarina.instruction.PoolType;
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
            Long poolDepth,
            Long poolLength,
            Long poolWidth,
            Long poolDiameter,
            Long poolVolume
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
            Long poolDepth,
            Long poolLength,
            Long poolWidth,
            Long poolDiameter,
            Long poolVolume
    ) {
        return pollInfoRepository
                .findByUserId(userId)
                .map(value ->
                        poolInfoTool.update(
                                value,
                                poolType,
                                poolDepth,
                                poolLength,
                                poolWidth,
                                poolDiameter,
                                poolVolume
                        )
                )
                .map(pollInfoRepository::update)
                .map(Result::<PoolInfo, Error>ok)
                .orElseGet(() -> Result.error(new NotFound("this poolInfo not found")));
    }

    public Result<PoolInfo, Error> create(PoolInfo poolInfo) {
        return Result.ok(
                pollInfoRepository.save(poolInfo)
        );
    }

    public Result<PoolInfo, Error> createOrUpdate(PoolInfo info) {
        return update(info.getUserId(), info.getPoolType(), 0L, 0L, 0L, 0L, 0L)
                .or(error -> {
                    if(error instanceof NotFound){
                        return create(info);
                    } else {
                        return Result.error(error);
                    }
                });
    }
}
