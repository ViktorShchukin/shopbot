package ru.aquamarina.config;

import io.micronaut.context.MessageSource;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.i18n.ResourceBundleMessageSource;
import io.pebbletemplates.pebble.PebbleEngine;
import jakarta.inject.Singleton;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.ResourceBundle;

@Factory
public class AppConfig {

    @Singleton
    PebbleEngine pebbleEngine() {
        return new PebbleEngine.Builder().build();
    }

    @Singleton
    MessageSource messageSource() {
        return new ResourceBundleMessageSource("i18n.messages");
    }
}
