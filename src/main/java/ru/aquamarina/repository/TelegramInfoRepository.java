package ru.aquamarina.repository;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.PageableRepository;
import ru.aquamarina.model.entity.TelegramInfo;
import ru.aquamarina.model.entity.User;

import java.util.Optional;
import java.util.UUID;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface TelegramInfoRepository extends PageableRepository<TelegramInfo, Long> {

    @Query("select gen_random_uuid()")
    //todo end this
    Optional<UUID> getUserIdByTelegramId(long id);
}
