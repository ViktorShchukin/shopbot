package ru.aquamarina.repository;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.PageableRepository;
import ru.aquamarina.model.UserRole;
import ru.aquamarina.model.entity.TelegramInfo;
import ru.aquamarina.model.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface TelegramInfoRepository extends PageableRepository<TelegramInfo, Long> {

    Optional<UUID> getUserIdByTelegramId(long id);

    Optional<TelegramInfo> findByUserId(UUID id);

    @Query("""
            select * from user_telegram_info uti
            	join app_user au on au.id = uti.user_id
            	where au.user_role = :userRole
            """)
    List<TelegramInfo> getByUserRole(UserRole userRole);
}
