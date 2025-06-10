package ru.aquamarina.service;

import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import ru.aquamarina.mapper.OrderTool;
import ru.aquamarina.model.DistributionMode;
import ru.aquamarina.model.entity.*;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotFound;
import ru.aquamarina.repository.OrderRepository;
import ru.aquamarina.util.Result;

import java.util.List;
import java.util.UUID;

@Singleton
public class OrderServiceWithExc {

    private final OrderRepository orderRepository;
    private final BasketServiceWithExc basketService;
    private final OrderTool orderTool;

    public OrderServiceWithExc(OrderRepository orderRepository, BasketServiceWithExc basketService, OrderTool orderTool) {
        this.orderRepository = orderRepository;
        this.basketService = basketService;
        this.orderTool = orderTool;
    }

    @Transactional
    public Order create(User user, String phoneNumber, String address, DistributionMode distributionMode) {
        Order order = orderRepository.save(
                orderTool.create(user.getId(), phoneNumber, address, distributionMode)
        );
        return order;
    }

    @Transactional
    public Result<Order, Error> update(Order order, String phoneNumber, String address, DistributionMode distributionMode) {
            Order updated = orderRepository.update(
                    orderTool.update(order, phoneNumber, address, distributionMode)
            );
            return Result.ok(updated);
    }

    @Transactional
    public Result<Order, Error> fillTheOrderAndClearBasket(Order order, Basket basket) {
            List<BasketRow> rows = basketService.getBasketRow(basket);
            rows.forEach(row -> orderRepository.addToOrder(order.getId(), row.getProductId(), row.getQuantity()));
            basketService.clearBasket(basket);
            return Result.ok(order);
    }

    public List<OrderRow> getOrderRow(Order order) {
        return orderRepository.getBasketRowByBasketId(order.getId());
    }

    public Result<Order, Error> findById(UUID orderId) {
            return orderRepository.findById(orderId)
                    .map(Result::<Order, Error>ok)
                    .orElseGet(() -> Result.error(new NotFound("Order not found")));
    }
}
