package ru.aquamarina.api.rest;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.api.rest.dto.CreateProductDto;
import ru.aquamarina.api.rest.dto.ProductDto;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.ExceptionWrapperError;
import ru.aquamarina.model.error.IoError;
import ru.aquamarina.model.error.NotFound;
import ru.aquamarina.service.ProductService;
import ru.aquamarina.util.Result;
import ru.aquamarina.util.ResultError;
import ru.aquamarina.util.ResultOk;

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
        Result<ProductDto, Error> res = productService.getById(id)
                .mapValue(productMapper::mapTo);
        return switch (res) {
            case ResultOk<ProductDto, Error> ok -> HttpResponse.ok(ok.result());
            // todo make better err handling
            case ResultError<ProductDto, Error> error -> {
                Error err = error.err();
                if (err instanceof IoError e) {
                    log.error("data access error during updateProduct request: ", e.e());
                } else if (err instanceof ExceptionWrapperError e) {
                    log.error("error during updateProduct request: ", e.exception());
                } else if (err instanceof NotFound e) {
                    log.error("error during updateProduct request: {}", e.message());
                    yield HttpResponse.notFound();
                } else {
                    log.error("error during updateProduct request");
                }
                yield HttpResponse.serverError();
            }
        };
    }

    @Post
    public HttpResponse<ProductDto> addProduct(@Body CreateProductDto dto) {
        if (!isValidFolderPath(dto.getPath())) {
            // todo make all response entities inherited from response interface???
            // cons are that it can not be normally deserialized??? check this
            // and return proper response with good message problem reason.
            return HttpResponse.badRequest();
        }
        Result<ProductDto, Error> res = productService
                .create(dto.getName(), dto.getCost(), dto.getDescription(), dto.getPath(), dto.getItemCode(), dto.getShortName())
                .mapValue(productMapper::mapTo);

        return switch (res) {
            case ResultOk<ProductDto, Error> ok -> HttpResponse.ok(ok.result());
            // todo make better err handling
            case ResultError<ProductDto, Error> error -> {
                Error err = error.err();
                if (err instanceof IoError e) {
                    log.error("data access error during addProduct request: ", e.e());
                } else if (err instanceof ExceptionWrapperError e) {
                    log.error("error during addProduct request: ", e.exception());
                } else {
                    log.error("error during addProduct request");
                }
                yield HttpResponse.serverError();
            }
        };
    }

    @Put("/{id}")
    public HttpResponse<ProductDto> updateProduct(@PathVariable UUID id,
                                                  @Body ProductDto dto) {
        if (!isValidFolderPath(dto.getPath())) {
            // todo make all response entities inherited from response interface???
            // cons are that it can not be normally deserialized??? check this
            // and return proper response with good message problem reason.
            return HttpResponse.badRequest();
        }
        Result<ProductDto, Error> res = productService.getById(id)
                .map(product -> productService.update(product,
                        dto.getName(),
                        dto.getCost(),
                        dto.getDescription(),
                        dto.getPath(),
                        dto.getItemCode(),
                        dto.getShortName()
                ))
                .mapValue(productMapper::mapTo);

        return switch (res) {
            case ResultOk<ProductDto, Error> ok -> HttpResponse.ok(ok.result());
            // todo make better err handling
            case ResultError<ProductDto, Error> error -> {
                Error err = error.err();
                if (err instanceof IoError e) {
                    log.error("data access error during updateProduct request: ", e.e());
                } else if (err instanceof ExceptionWrapperError e) {
                    log.error("error during updateProduct request: ", e.exception());
                } else if (err instanceof NotFound e) {
                    log.error("error during updateProduct request: {}", e.message());
                    yield HttpResponse.notFound();
                } else {
                    log.error("error during updateProduct request");
                }
                yield HttpResponse.serverError();
            }
        };
    }

    @Delete("/{id}")
    public HttpResponse<?> deleteProduct(@PathVariable UUID id) {
        productService.delete(id);
        return HttpResponse.noContent();
    }

    private boolean isValidFolderPath(String folderPath) {
        return folderPath.getBytes().length < 60;
    }
}
