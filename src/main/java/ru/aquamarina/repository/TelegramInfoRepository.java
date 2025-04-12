package ru.aquamarina.repository;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.PageableRepository;
import ru.aquamarina.model.TelegramInfo;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface TelegramInfoRepository extends PageableRepository<TelegramInfo, Long> {
}
