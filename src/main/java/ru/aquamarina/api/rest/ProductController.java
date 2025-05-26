package ru.aquamarina.api.rest;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.api.rest.dto.ProductDto;
import ru.aquamarina.service.ProductService;

import java.util.List;
import java.util.UUID;

@Controller("/product")
public class ProductController {

    private final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;
    private final ProductMapper productMapper;

    public ProductController(ProductService productService,
                             ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @Get
    public HttpResponse<List<ProductDto>> getAll() {
        var res = productMapper.mapTo(productService.getAll());
        return HttpResponse.ok(res);
    }

    @Get("/{id}")
    public HttpResponse<ProductDto> getById(@PathVariable UUID id) {
        return productService.getById(id)
                .map(productMapper::mapTo)
                .map(HttpResponse::ok)
                .orElseGet(HttpResponse::notFound);
    }

    @Post
    public HttpResponse<ProductDto> addProduct(@RequestBean ProductDto dto) {
        var res = productService.create(dto.getName(), dto.getCost(), dto.getDescription(), dto.getPath());
        return HttpResponse.ok(productMapper.mapTo(res));
    }

    @Put("/{id}")
    public HttpResponse<ProductDto> updateProduct(@PathVariable UUID id,
                                                  @RequestBean ProductDto dto) {
        return productService.getById(id)
                .map(product -> productService.update(product, dto.getName(), dto.getCost(), dto.getDescription(), dto.getPath()))
                .map(productMapper::mapTo)
                .map(HttpResponse::ok)
                .orElseGet(HttpResponse::notFound);
    }

    @Delete("/{id}")
    public HttpResponse<?> deleteProduct(@PathVariable UUID id) {
        productService.delete(id);
        return HttpResponse.noContent();
    }
}
