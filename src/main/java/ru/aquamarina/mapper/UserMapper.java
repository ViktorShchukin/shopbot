package ru.aquamarina.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.aquamarina.config.AppMapperConfig;
import ru.aquamarina.model.entity.User;

@Mapper(config = AppMapperConfig.class)
public interface UserMapper {

    public static UserMapper getInstance() {
        return Mappers.getMapper(UserMapper.class);
    }

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    User create(String login, String lastState);
}
