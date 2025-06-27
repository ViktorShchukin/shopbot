package ru.aquamarina.service;

import io.micronaut.data.exceptions.DataAccessException;
import jakarta.inject.Singleton;
import ru.aquamarina.model.DistributionMode;
import ru.aquamarina.model.entity.*;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.IoError;
import ru.aquamarina.util.Result;

import java.util.List;
import java.util.UUID;

@Singleton
public class OrderService {

    private final OrderServiceWithExc orderServiceWithExc;

    public OrderService(OrderServiceWithExc orderServiceWithExc) {
        this.orderServiceWithExc = orderServiceWithExc;
    }

    public Order create(User user, String phoneNumber, String address, DistributionMode distributionMode, String additionalInfo) {
        return orderServiceWithExc.create(user, phoneNumber, address, distributionMode, additionalInfo);
    }

    public Result<Order, Error> update(Order order,
                                       String phoneNumber,
                                       String address,
                                       DistributionMode distributionMode,
                                       String additionalInfo) {
        try {
            return orderServiceWithExc.update(order, phoneNumber, address, distributionMode, additionalInfo);
        } catch (DataAccessException e) {
            return Result.error(new IoError(e));
        }
    }

    public Result<Order, Error> fillTheOrderAndClearBasket(Order order, Basket basket) {
        try {
            return orderServiceWithExc.fillTheOrderAndClearBasket(order, basket);
        } catch (DataAccessException e) {
            return Result.error(new IoError(e));
        }
    }

    public List<OrderRow> getOrderRow(Order order) {
        return orderServiceWithExc.getOrderRow(order);
    }

    public Result<Order, Error> findById(UUID orderId) {
        try {
            return orderServiceWithExc.findById(orderId);
        } catch (DataAccessException e) {
            return Result.error(new IoError(e));
        }
    }
}
