package ru.aquamarina.service;

import io.micronaut.data.exceptions.DataAccessException;
import jakarta.inject.Singleton;
import ru.aquamarina.mapper.ProductUtil;
import ru.aquamarina.model.entity.Product;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.IoError;
import ru.aquamarina.model.error.NotFound;
import ru.aquamarina.model.error.ProductNotFound;
import ru.aquamarina.repository.ProductRepository;
import ru.aquamarina.util.PathUtil;
import ru.aquamarina.util.Result;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductUtil productUtil;

    public ProductService(ProductRepository productRepository, ProductUtil productUtil) {
        this.productRepository = productRepository;
        this.productUtil = productUtil;
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Result<Product, Error> create(String name, long cost, String description, String path, Long itemCode) {
        Product created = productUtil.create(name, cost, description, path, itemCode);
        try {
            return Result.ok(productRepository.save(created));
        } catch (DataAccessException e) {
            return Result.error(new IoError(e));
        }
    }

    public Result<Product, Error> update(Product product, String name, long cost, String description, String path, Long itemCode) {
        Product updated = productUtil.update(product, name, cost, description, path, itemCode);
        try {
            return Result.ok(productRepository.update(updated));
        } catch (DataAccessException e) {
            return Result.error(new IoError(e));
        }
    }

    public void delete(UUID id) {
        productRepository.deleteById(id);
    }

    public Result<Product, Error> getById(UUID id) {
        try {
            return productRepository.findById(id)
                    .map(Result::<Product, Error>ok)
                    .orElseGet(() -> Result.error(new NotFound("product not found, id: %s".formatted(id))));
        } catch (DataAccessException e) {
            return Result.error(new IoError(e));
        }
    }

    public Result<Product, Error> getByName(String name) {
        return productRepository.findByName(name)
                .map(Result::<Product, Error>ok)
                .orElseGet(() -> Result.error(new ProductNotFound()));
    }

    public List<Product> getByPathLike(String path) {
        String pattern = "%s%%".formatted(path);
        return productRepository.findByPathLike(pattern);
    }
}
