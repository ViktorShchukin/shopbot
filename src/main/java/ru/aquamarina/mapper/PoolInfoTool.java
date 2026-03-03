package ru.aquamarina.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.aquamarina.config.AppMapperConfig;
import ru.aquamarina.instruction.PoolType;
import ru.aquamarina.model.entity.PoolInfo;

@Mapper(config = AppMapperConfig.class)
public interface PoolInfoTool {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    PoolInfo update(
            @MappingTarget PoolInfo poolInfo,
            PoolType poolType,
            Long poolDepth,
            Long poolLength,
            Long poolWidth,
            Long poolDiameter,
            Long poolVolume
    );
}
