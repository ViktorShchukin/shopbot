package ru.aquamarina.mapper;

import io.micronaut.validation.Validated;
import jakarta.validation.constraints.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.aquamarina.config.AppMapperConfig;
import ru.aquamarina.model.entity.TelegramInfo;

import java.util.UUID;

@Validated // todo check will it work on the generated code?
@Mapper(config = AppMapperConfig.class)
public interface TelegramInfoUtil {

    TelegramInfo create(long telegramId,
                        @NotNull UUID userId,
                        String firstName,
                        String lastName,
                        String userName,
                        Boolean updated);

    @Mapping(target = "telegramId", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "updated", expression = "java(Boolean.TRUE)")
    TelegramInfo update(@MappingTarget TelegramInfo telegramInfo,
                        String firstName,
                        String lastName,
                        String userName);
}
