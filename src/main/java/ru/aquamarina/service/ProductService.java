package ru.aquamarina.service;

import jakarta.inject.Singleton;
import ru.aquamarina.model.Product;
import ru.aquamarina.repository.ProductRepository;

import java.util.List;
import java.util.UUID;

@Singleton
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAll(){
        return productRepository.findAll();
    }

    public Product create(){
        // todo
        return null;
    }

    public Product update(){
        // todo
        return null;
    }

    public void delete(UUID id){
        productRepository.deleteById(id);
    }

}
