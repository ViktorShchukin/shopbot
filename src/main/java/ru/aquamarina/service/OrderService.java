package ru.aquamarina.service;

import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import ru.aquamarina.mapper.OrderTool;
import ru.aquamarina.model.entity.*;
import ru.aquamarina.repository.OrderRepository;

import java.util.List;

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

    public List<OrderRow> getOrderRow(User user) {
        // todo think about it. should not just call get(). And it may be a lot of orders here.
        // should get order by orderId
        var order = orderRepository.findByUserId(user.getId()).get();
        return orderRepository.getBasketRowByBasketId(order.getId());
    }
}
