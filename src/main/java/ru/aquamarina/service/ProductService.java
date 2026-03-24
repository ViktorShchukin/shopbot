package ru.aquamarina.service;

import io.micronaut.data.exceptions.DataAccessException;
import jakarta.inject.Singleton;
import ru.aquamarina.model.entity.Product;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.IoError;
import ru.aquamarina.util.Result;

import java.util.List;
import java.util.UUID;

@Singleton
public class ProductService {

    private final ProductServiceWithExc productServiceWithExc;

    public ProductService(ProductServiceWithExc productServiceWithExc) {
        this.productServiceWithExc = productServiceWithExc;
    }

    public List<Product> getAll() {
        return productServiceWithExc.getAll();
    }

    public Result<Product, Error> create(String name, long cost, String description, String path, Long itemCode, String shortName) {
        try {
            return productServiceWithExc.create(name, cost, description, path, itemCode, shortName);
        } catch (DataAccessException e) {
            return Result.error(new IoError(e));
        }
    }

    public Result<Product, Error> update(Product product, String name, long cost, String description, String path, Long itemCode, String shortName) {
        try {
            return productServiceWithExc.update(product, name, cost, description, path, itemCode, shortName);
        } catch (DataAccessException e) {
            return Result.error(new IoError(e));
        }
    }

    public void delete(UUID id) {
        productServiceWithExc.delete(id);
    }

    public Result<Product, Error> getById(UUID id) {
        try {
            return productServiceWithExc.getById(id);
        } catch (DataAccessException e) {
            return Result.error(new IoError(e));
        }
    }

    public Result<Product, Error> getByName(String name) {
        return productServiceWithExc.getByName(name);
    }

    public List<Product> getByPathLike(String path) {
        return productServiceWithExc.getByPathLike(path);
    }
}
