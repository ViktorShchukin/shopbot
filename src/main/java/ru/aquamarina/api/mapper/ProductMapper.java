package ru.aquamarina.api.mapper;

import org.mapstruct.Mapper;
import ru.aquamarina.api.dto.ProductRowDto;
import ru.aquamarina.model.entity.BasketRow;
import ru.aquamarina.model.entity.OrderRow;
import ru.aquamarina.model.entity.Product;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.util.Result;

import java.util.UUID;
import java.util.function.Function;

@Mapper
public interface ProductMapper {

    default Result<ProductRowDto, Error> mapTo(BasketRow basketRow, Function<UUID, Result<Product, Error>> productGetter) {
        return productGetter
                .apply(basketRow.getProductId())
                .mapValue(product -> new ProductRowDto(product, basketRow.getQuantity()));
    }

    default Result<ProductRowDto, Error> mapTo(OrderRow orderRow, Function<UUID, Result<Product, Error>> productGetter) {
        return productGetter
                .apply(orderRow.getProductId())
                .mapValue(product -> new ProductRowDto(product, orderRow.getQuantity()));
    }
}
