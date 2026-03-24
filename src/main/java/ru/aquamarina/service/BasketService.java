package ru.aquamarina.service;

import io.micronaut.data.exceptions.DataAccessException;
import jakarta.inject.Singleton;
import ru.aquamarina.model.entity.Basket;
import ru.aquamarina.model.entity.BasketRow;
import ru.aquamarina.model.entity.Product;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.ExceptionWrapperError;
import ru.aquamarina.model.error.IoError;
import ru.aquamarina.util.Result;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class BasketService {

    private final BasketServiceWithExc basketServiceWithExc;

    public BasketService(BasketServiceWithExc basketServiceWithExc) {
        this.basketServiceWithExc = basketServiceWithExc;
    }

    public Result<Basket, Error> getByUser(User user) {
        try {
            return basketServiceWithExc.getByUser(user);
        } catch (DataAccessException e) {
            return Result.error(new ExceptionWrapperError(e, "error in BasketService::getByUser"));
        }
    }

    public Optional<Basket> getByUserId(UUID userId) {
        return basketServiceWithExc.getByUserId(userId);
    }

    public Basket create(User user) {
        return basketServiceWithExc.create(user);
    }

    // todo make this to return result
    public void addToBasket(User user, Product product, long productQuantity) {
        // todo make create() return Result
        basketServiceWithExc.addToBasket(user, product.getId(), productQuantity);

    }

    public Result<Long, Error> addToBasket(User user, UUID productId, long productQuantity) {
        try {
            return basketServiceWithExc.addToBasket(user, productId, productQuantity);
        } catch (DataAccessException e) {
            return Result.error(new IoError(e));
        } catch (NoSuchElementException e) {
            return Result.error(new ExceptionWrapperError(e,
                    "this exception should not appear because i user Optinal::or in chain above"
            ));
        }
    }

    public List<BasketRow> getBasketRow(User user) {
        return basketServiceWithExc.getBasketRow(user);
    }

    public List<BasketRow> getBasketRow(Basket basket) {
        return basketServiceWithExc.getBasketRow(basket);
    }

    public Result<BasketRow, Error> getBasketRow(User user, UUID productId) {
        try {
            return basketServiceWithExc.getBasketRow(user, productId);
        } catch (DataAccessException e) {
            return Result.error(new IoError(e));
        }

    }

    public void clearBasket(Basket basket) {
        basketServiceWithExc.clearBasket(basket);
    }

    public Result<Long, Error> deleteFromBasket(User user, Product product) {
        return basketServiceWithExc.deleteFromBasket(user, product);
    }

    public Result<Long, Error> deleteFromBasket(User user, UUID productId) {
        try {
            return basketServiceWithExc.deleteFromBasket(user, productId);
        } catch (DataAccessException e) {
            return Result.error(new IoError(e));
        }
    }
}
