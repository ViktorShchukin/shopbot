package ru.aquamarina.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.aquamarina.config.AppMapperConfig;
import ru.aquamarina.model.DistributionMode;
import ru.aquamarina.model.entity.Order;

import java.util.UUID;

@Mapper(config = AppMapperConfig.class)
public interface OrderTool {


    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "userId", source = "userUuid")
    Order create(UUID userUuid, String phoneNumber, String address, DistributionMode distributionMode);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    Order update(@MappingTarget Order order, String phoneNumber, String address, DistributionMode distributionMode);
}
