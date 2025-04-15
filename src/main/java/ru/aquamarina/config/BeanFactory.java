package ru.aquamarina.config;

import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;
import ru.aquamarina.mapper.UserMapper;

@Factory
public class BeanFactory {

    @Singleton
    UserMapper getUserMapper(){
        return UserMapper.getInstance();
    }
}
