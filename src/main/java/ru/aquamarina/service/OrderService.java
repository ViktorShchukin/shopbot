package ru.aquamarina.service;

import io.micronaut.data.exceptions.DataAccessException;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import ru.aquamarina.mapper.OrderTool;
import ru.aquamarina.model.entity.*;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.IoError;
import ru.aquamarina.model.error.NotFound;
import ru.aquamarina.repository.OrderRepository;
import ru.aquamarina.util.Result;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class OrderService {

    private final OrderRepository orderRepository;
    private final BasketService basketService;
    private final OrderTool orderTool;

    public OrderService(OrderRepository orderRepository, BasketService basketService, OrderTool orderTool) {
        this.orderRepository = orderRepository;
        this.basketService = basketService;
        this.orderTool = orderTool;
    }

    @Transactional
    public Order create(Basket basket) {
        Order order = orderRepository.save(
                orderTool.create(basket.getUserId())
        );
        List<BasketRow> rows = basketService.getBasketRow(basket);
        rows.forEach(row -> orderRepository.addToOrder(order.getId(), row.getProductId(), row.getQuantity()));
        basketService.cleanBasket(basket);
        return order;
    }

    public List<OrderRow> getOrderRow(Order order) {
        return orderRepository.getBasketRowByBasketId(order.getId());
    }

    public Result<Order, Error> findById(UUID orderId) {
        try {
            return orderRepository.findById(orderId)
                    .map(Result::<Order, Error>ok)
                    .orElseGet(() -> Result.error(new NotFound("Order not found")));
        } catch (DataAccessException e) {
            return Result.error(new IoError(e));
        }
    }
}
