package ru.aquamarina.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.aquamarina.config.AppMapperConfig;
import ru.aquamarina.guide.FilterType;
import ru.aquamarina.guide.PoolType;
import ru.aquamarina.guide.dto.PoolInfoDto;
import ru.aquamarina.model.entity.PoolInfo;

@Mapper(config = AppMapperConfig.class)
public interface PoolInfoTool {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    PoolInfo update(
            @MappingTarget PoolInfo poolInfo,
            FilterType filterType,
            PoolType poolType,
            Double poolDepth,
            Double poolLength,
            Double poolWidth,
            Double poolDiameter,
            Double poolVolume
    );

    PoolInfoDto map(PoolInfo poolInfo);
}
