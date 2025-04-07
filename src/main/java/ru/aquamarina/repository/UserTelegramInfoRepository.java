package ru.aquamarina.repository;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.PageableRepository;
import ru.aquamarina.model.UserTelegramInfo;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface UserTelegramInfoRepository extends PageableRepository<UserTelegramInfo, Long> {
}
