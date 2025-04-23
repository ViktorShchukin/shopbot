package ru.aquamarina.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.aquamarina.config.AppMapperConfig;
import ru.aquamarina.model.entity.Product;

@Mapper(config = AppMapperConfig.class)
public interface ProductUtil {

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    Product create(String name, long cost, String description);

    @Mapping(target = "id", ignore = true)
    Product update(@MappingTarget Product product, String name, long cost, String description);
}