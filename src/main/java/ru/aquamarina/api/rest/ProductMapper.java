package ru.aquamarina.api.rest;

import org.mapstruct.Mapper;
import ru.aquamarina.api.rest.dto.ProductDto;
import ru.aquamarina.config.AppMapperConfig;
import ru.aquamarina.model.entity.Product;

import java.util.List;

@Mapper(config = AppMapperConfig.class)
public interface ProductMapper {

    Product mapFrom(ProductDto dto);

    ProductDto mapTo(Product product);

    List<Product> mapFrom(List<ProductDto> dto);

    List<ProductDto> mapTo(List<Product> product);
}
