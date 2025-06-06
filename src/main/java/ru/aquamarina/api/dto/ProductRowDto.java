package ru.aquamarina.api.dto;

import ru.aquamarina.model.entity.Product;

public record ProductRowDto(Product product, Long quantity) {
}
