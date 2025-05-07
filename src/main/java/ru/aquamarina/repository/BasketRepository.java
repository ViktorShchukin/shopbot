package ru.aquamarina.repository;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.PageableRepository;
import ru.aquamarina.model.entity.Basket;
import ru.aquamarina.model.entity.BasketRow;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface BasketRepository extends PageableRepository<Basket, UUID> {
    Optional<Basket> findByUserId(UUID userId);

    @Query("insert into basket_and_product (basket_id, product_id, quantity) values (:basketId, :productId, :productQuantity)")
    Integer addToBasket(UUID basketId, UUID productId, long productQuantity);

    @Query("select bap.basket_id, bap.product_id, bap.quantity from basket_and_product bap where bap.basket_id = :basketId")
    List<BasketRow> getBasketRowByBasketId(UUID basketId);

    @Query("delete from basket_and_product where basket_id = :basketId")
    void deleteAllFromBasket(UUID basketId);
}
