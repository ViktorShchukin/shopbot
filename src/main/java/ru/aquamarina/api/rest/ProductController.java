package ru.aquamarina.api.rest;

import io.micronaut.configuration.jdbc.hikari.DatasourceConfiguration;
import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.model.Product;
import ru.aquamarina.service.ProductService;

import java.util.List;

@Controller("/product")
public class ProductController {

    private final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;
    private final DatasourceConfiguration datasourceConfiguration;
    private final ApplicationContext applicationContext;

    public ProductController(ProductService productService, DatasourceConfiguration datasourceConfiguration, ApplicationContext applicationContext) {
        this.productService = productService;
        this.datasourceConfiguration = datasourceConfiguration;
        this.applicationContext = applicationContext;
    }

    @Get
    public HttpResponse<List<Product>> getAll(){
        var some = datasourceConfiguration.getDataSource();
//        var some1 = applicationContext;
        var res = productService.getAll();
        return HttpResponse.ok(res);
    }
}
