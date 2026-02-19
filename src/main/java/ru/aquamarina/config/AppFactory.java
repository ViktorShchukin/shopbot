package ru.aquamarina.config;

import io.micronaut.context.annotation.Factory;
import io.pebbletemplates.pebble.PebbleEngine;
import jakarta.inject.Singleton;

@Factory
public class AppFactory {

    @Singleton
    PebbleEngine pebbleEngine(){
        return new PebbleEngine.Builder().build();
    }
}
