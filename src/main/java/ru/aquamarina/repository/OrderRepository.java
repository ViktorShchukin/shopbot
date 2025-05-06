package ru.aquamarina.repository;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.PageableRepository;
import ru.aquamarina.model.entity.Basket;
import ru.aquamarina.model.entity.BasketRow;
import ru.aquamarina.model.entity.Order;
import ru.aquamarina.model.entity.OrderRow;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface OrderRepository extends PageableRepository<Order, UUID> {

    Optional<Order> findByUserId(UUID userId);

    @Query("insert into order_and_product (order_id, product_id, quantity) values (:orderId, :productId, :productQuantity)")
    Integer addToOrder(UUID orderId, UUID productId, long productQuantity);

    @Query("select oap.order_id, oap.product_id, oap.quantity from order_and_product oap where oap.order_id = :orderId")
    List<OrderRow> getBasketRowByBasketId(UUID orderId);
}
