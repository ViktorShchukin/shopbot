package ru.aquamarina.util;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.aquamarina.config.AppMapperConfig;
import ru.aquamarina.model.entity.Basket;

import java.util.UUID;

@Mapper(config = AppMapperConfig.class)
public interface BasketMapper {

    @Mapping(target = "id" , expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "userId", source = "userId")
    Basket create(UUID userId);
}
