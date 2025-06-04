package ru.aquamarina.service;

import io.micronaut.data.exceptions.DataAccessException;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import ru.aquamarina.model.entity.Basket;
import ru.aquamarina.model.entity.BasketRow;
import ru.aquamarina.model.entity.Product;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.ExceptionWrapperError;
import ru.aquamarina.model.error.NotFound;
import ru.aquamarina.repository.BasketRepository;
import ru.aquamarina.util.Result;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class BasketService {

    private final BasketRepository basketRepository;


    public BasketService(BasketRepository basketRepository) {
        this.basketRepository = basketRepository;
    }

    public Result<Basket, Error> getByUser(User user) {
        try {
            return getByUserId(user.getId())
                    .map(bsk -> Result.<Basket, Error>ok(bsk))
                    .orElseGet(() -> Result.error(new NotFound("basket for this user not found")));
        } catch (DataAccessException e) {
            return Result.error(new ExceptionWrapperError(e, "error in BasketService::getByUser"));
        }
    }

    public Optional<Basket> getByUserId(UUID userId) {
        return basketRepository.findByUserId(userId);
    }

    @Transactional
    public Basket create(User user) {
        // todo make creation of entity with mapper
        Basket created = new Basket();
        created.setId(UUID.randomUUID());
        created.setUserId(user.getId());
        return basketRepository.save(created);
    }

    @Transactional
    public void addToBasket(User user, Product product, long productQuantity) {
        // todo make create() return Result
        basketRepository.findByUserId(user.getId())
                .or(() -> java.util.Optional.ofNullable(create(user)))
                .map(basket -> {
                    boolean isRowExist = basketRepository.existByBasketIdProductId(basket.getId(), product.getId());
                    if (isRowExist) {
                        return basketRepository.updateRowQuantity(basket.getId(), product.getId(), productQuantity);
                    } else {
                        return basketRepository.addToBasket(basket.getId(), product.getId(), productQuantity);
                    }
                });

    }

    @Transactional
    public List<BasketRow> getBasketRow(User user) {
        var basket = basketRepository.findByUserId(user.getId())
                .orElseGet(() -> create(user));
        return basketRepository.getBasketRowByBasketId(basket.getId());
    }

    @Transactional
    public List<BasketRow> getBasketRow(Basket basket) {
        return basketRepository.getBasketRowByBasketId(basket.getId());
    }

    public void clearBasket(Basket basket) {
        basketRepository.deleteAllFromBasket(basket.getId());
    }

    public Result<Integer, Error> deleteFromBasket(User user, Product product) {
        try {
            return basketRepository.findByUserId(user.getId())
                    .map(basket -> {
                        if (basketRepository.existByBasketIdProductId(basket.getId(), product.getId())) {
                            var deletedQuantity = basketRepository.deleteFromBasketByProductId(basket.getId(), product.getId());
                            return Result.<Integer, Error>ok(deletedQuantity);
                        } else {
                            return Result.<Integer, Error>ok(0);
                        }
                    })
                    .orElseGet(() -> Result.error(new NotFound("basket for this user is not exist")));
        } catch (DataAccessException e) {
            return Result.error(new ExceptionWrapperError(e, "data access error during BasketService::deleteFromBasket"));
        }
    }
}
