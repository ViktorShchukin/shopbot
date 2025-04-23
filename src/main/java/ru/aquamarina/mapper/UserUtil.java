package ru.aquamarina.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import ru.aquamarina.config.AppMapperConfig;
import ru.aquamarina.fsm.state.FsmState;
import ru.aquamarina.model.entity.User;

@Mapper(config = AppMapperConfig.class)
public interface UserUtil {

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    User create(String login, String lastState);

    @Mapping(target = "id", ignore = true)
    User update(@MappingTarget User user, String login, FsmState lastState);

    default String mapState(FsmState state) {
        return state.toString();
    }
}
