package ru.aquamarina.service;

import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import ru.aquamarina.mapper.ProductUtil;
import ru.aquamarina.model.entity.Product;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotFound;
import ru.aquamarina.model.error.ProductNotFound;
import ru.aquamarina.repository.ProductRepository;
import ru.aquamarina.util.Result;

import java.util.List;
import java.util.UUID;

@Singleton
public class ProductServiceWithExc {


    private final ProductRepository productRepository;
    private final ProductUtil productUtil;

    public ProductServiceWithExc(ProductRepository productRepository, ProductUtil productUtil) {
        this.productRepository = productRepository;
        this.productUtil = productUtil;
    }

    @Transactional
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Transactional
    public Result<Product, Error> create(String name, long cost, String description, String path, Long itemCode, String shortName) {
        Product created = productUtil.create(name, cost, description, path, itemCode, shortName);
        return Result.ok(productRepository.save(created));
    }

    @Transactional
    public Result<Product, Error> update(Product product, String name, long cost, String description, String path, Long itemCode, String shortName) {
        Product updated = productUtil.update(product, name, cost, description, path, itemCode, shortName);
        return Result.ok(productRepository.update(updated));
    }

    @Transactional
    public void delete(UUID id) {
        productRepository.deleteById(id);
    }

    @Transactional
    public Result<Product, Error> getById(UUID id) {
        return productRepository.findById(id)
                .map(Result::<Product, Error>ok)
                .orElseGet(() -> Result.error(new NotFound("product not found, id: %s".formatted(id))));
    }

    @Transactional
    public Result<Product, Error> getByName(String name) {
        return productRepository.findByName(name)
                .map(Result::<Product, Error>ok)
                .orElseGet(() -> Result.error(new ProductNotFound()));
    }

    @Transactional
    public List<Product> getByPathLike(String path) {
        String pattern = "%s%%".formatted(path);
        return productRepository.findByPathLike(pattern);
    }
}
