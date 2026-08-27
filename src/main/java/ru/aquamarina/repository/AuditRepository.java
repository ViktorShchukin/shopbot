package ru.aquamarina.repository;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;
import ru.aquamarina.model.entity.UserAuditEntry;

import java.util.UUID;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface AuditRepository extends CrudRepository<UserAuditEntry, UUID> {
}
