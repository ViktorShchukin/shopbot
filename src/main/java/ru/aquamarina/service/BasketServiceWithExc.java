package ru.aquamarina.service;

import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import ru.aquamarina.model.entity.Basket;
import ru.aquamarina.model.entity.BasketRow;
import ru.aquamarina.model.entity.Product;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotFound;
import ru.aquamarina.repository.BasketRepository;
import ru.aquamarina.util.BasketMapper;
import ru.aquamarina.util.Result;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class BasketServiceWithExc {

    private final BasketRepository basketRepository;
    private final BasketMapper basketMapper;


    public BasketServiceWithExc(BasketRepository basketRepository, BasketMapper basketMapper) {
        this.basketRepository = basketRepository;
        this.basketMapper = basketMapper;
    }

    @Transactional
    public Result<Basket, Error> getByUser(User user) {
            return getByUserId(user.getId())
                    .map(bsk -> Result.<Basket, Error>ok(bsk))
                    .orElseGet(() -> Result.error(new NotFound("basket for this user not found")));
    }

    @Transactional
    public Optional<Basket> getByUserId(UUID userId) {
        return basketRepository.findByUserId(userId);
    }

    @Transactional
    public Basket create(User user) {
        Basket created = basketMapper.create(user.getId());
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
            return getByUser(user)
                    .map(basket -> basketRepository
                            .findBasketRowByUserIdAndProductId(basket.getId(), productId)
                            .map(Result::<BasketRow, Error>ok)
                            .orElseGet(() -> Result.error(new NotFound("basket row not found BasketService::getBasketRow")))
                    );
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
    }
}
