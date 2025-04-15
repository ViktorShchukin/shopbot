package ru.aquamarina.mapper;

import io.micronaut.validation.Validated;
import jakarta.validation.constraints.NotNull;
import org.mapstruct.Mapper;
import ru.aquamarina.config.AppMapperConfig;
import ru.aquamarina.model.entity.TelegramInfo;

import java.util.UUID;

@Validated // todo check will it work on the generated code?
@Mapper(config = AppMapperConfig.class)
public interface TelegramInfoMapper {

    TelegramInfo create(long telegramId,@NotNull UUID userId);
}
