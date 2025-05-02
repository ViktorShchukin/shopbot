package ru.aquamarina.repository;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.PageableRepository;
import ru.aquamarina.model.entity.Basket;

import java.util.Optional;
import java.util.UUID;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface BasketRepository extends PageableRepository<Basket, UUID> {
    Optional<Basket> findByUserId(UUID userId);

    @Query("insert into basket (basket_id, product_id, quantity) values (:basketId, : productId, :productQuantity)")
    int addToBasket(UUID basketId, UUID productId, long productQuantity);
}
