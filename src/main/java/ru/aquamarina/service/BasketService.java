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
import ru.aquamarina.model.error.IoError;
import ru.aquamarina.model.error.NotFound;
import ru.aquamarina.repository.BasketRepository;
import ru.aquamarina.util.Result;

import java.util.List;
import java.util.NoSuchElementException;
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
    // todo make this to return result
    public void addToBasket(User user, Product product, long productQuantity) {
        // todo make create() return Result
        addToBasket(user, product.getId(), productQuantity);

    }

    @Transactional
    public Result<Long, Error> addToBasket(User user, UUID productId, long productQuantity) {
        try {
            return basketRepository.findByUserId(user.getId())
                    .or(() -> java.util.Optional.ofNullable(create(user)))
                    .map(basket -> {
                        boolean isRowExist = basketRepository.existByBasketIdProductId(basket.getId(), productId);
                        if (isRowExist) {
                            return (long) basketRepository.updateRowQuantity(basket.getId(), productId, productQuantity);
                        } else {
                            return (long) basketRepository.addToBasket(basket.getId(), productId, productQuantity);
                        }
                    })
                    .map(Result::<Long, Error>ok)
                    .get();
        } catch (DataAccessException e) {
            return Result.error(new IoError(e));
        } catch (NoSuchElementException e) {
            return Result.error(new ExceptionWrapperError(e,
                    "this exception should not appear because i user Optinal::or in chain above"
            ));
        }
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

    @Transactional
    public Result<BasketRow, Error> getBasketRow(User user, UUID productId) {
        try {
            return getByUser(user)
                    .map(basket -> basketRepository
                            .findBasketRowByUserIdAndProductId(basket.getId(), productId)
                            .map(Result::<BasketRow, Error>ok)
                            .orElseGet(() -> Result.error(new NotFound("basket row not found BasketService::getBasketRow")))
                    );
        } catch (DataAccessException e) {
            return Result.error(new IoError(e));
        }

    }

    @Transactional
    public void clearBasket(Basket basket) {
        basketRepository.deleteAllFromBasket(basket.getId());
    }

    @Transactional
    public Result<Long, Error> deleteFromBasket(User user, Product product) {
        return deleteFromBasket(user, product.getId());
    }

    @Transactional
    public Result<Long, Error> deleteFromBasket(User user, UUID productId) {
        try {
            return basketRepository.findByUserId(user.getId())
                    .map(basket -> {
                        if (basketRepository.existByBasketIdProductId(basket.getId(), productId)) {
                            var deletedQuantity = basketRepository.deleteFromBasketByProductId(basket.getId(), productId);
                            return Result.<Long, Error>ok((long) deletedQuantity);
                        } else {
                            return Result.<Long, Error>ok(0L);
                        }
                    })
                    .orElseGet(() -> Result.error(new NotFound("basket for this user is not exist")));
        } catch (DataAccessException e) {
            return Result.error(new IoError(e));
        }
    }
}
