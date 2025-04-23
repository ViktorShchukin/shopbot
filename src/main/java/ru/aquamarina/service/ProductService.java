package ru.aquamarina.service;

import jakarta.inject.Singleton;
import ru.aquamarina.mapper.ProductUtil;
import ru.aquamarina.model.entity.Product;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.ProductNotFound;
import ru.aquamarina.repository.ProductRepository;
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

    public Product create(String name, long cost, String description) {
        Product created = productUtil.create(name, cost, description);
        return productRepository.save(created);
    }

    public Product update(Product product, String name, long cost, String description) {
        Product updated = productUtil.update(product, name, cost, description);
        return productRepository.update(updated);
    }

    public void delete(UUID id) {
        productRepository.deleteById(id);
    }

    public Optional<Product> getById(UUID id) {
        return productRepository.findById(id);
    }

    public Result<Product, Error> getByName(String name) {
        return productRepository.findByName(name)
                .map(Result::<Product, Error>ok)
                .orElseGet(() -> Result.error(new ProductNotFound()));
    }
}
