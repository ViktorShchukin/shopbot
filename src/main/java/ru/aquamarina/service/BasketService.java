package ru.aquamarina.service;

import jakarta.inject.Singleton;
import ru.aquamarina.repository.BasketRepository;

@Singleton
public class BasketService {

    private final BasketRepository basketRepository;


    public BasketService(BasketRepository basketRepository) {
        this.basketRepository = basketRepository;
    }


}
