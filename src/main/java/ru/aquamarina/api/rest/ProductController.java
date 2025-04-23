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

//    @Post
//    public HttpResponse<ProductDto> addProduct(@RequestBean ProductDto dto) {
//
//    }

//    @Put("/{id}")
//    public HttpResponse<ProductDto> updateProduct(@PathVariable UUID id,
//                                                  @RequestBean ProductDto dto) {
//
//    }

//    @Delete("/{id}")
//    public HttpResponse<?> deleteProduct(@PathVariable UUID id) {
//
//    }
}
