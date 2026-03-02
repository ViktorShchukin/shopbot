package ru.aquamarina.repository;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.PageableRepository;
import ru.aquamarina.model.entity.PoolInfo;

import java.util.Optional;
import java.util.UUID;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface PollInfoRepository extends PageableRepository<PoolInfo, UUID> {
    Optional<PoolInfo> findByUserId(UUID userId);
}
