package ru.aquamarina.service;

import jakarta.inject.Singleton;
import ru.aquamarina.model.entity.Basket;
import ru.aquamarina.model.entity.Product;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.repository.BasketRepository;

import java.util.UUID;

@Singleton
public class BasketService {

    private final BasketRepository basketRepository;


    public BasketService(BasketRepository basketRepository) {
        this.basketRepository = basketRepository;
    }

    public Basket create(User user) {
        // todo make creation of entity with mapper
        Basket created = new Basket();
        created.setId(UUID.randomUUID());
        created.setUserId(user.getId());
       return basketRepository.save(created);
    }


    public void addToBasket(User user, Product product, long productQuantity) {
        // todo make create() return Result
        basketRepository.findByUserId(user.getId())
                .or(() -> java.util.Optional.ofNullable(create(user)))
                .map(basket -> basketRepository.addToBasket(basket.getId(), product.getId(), productQuantity));

    }
}
