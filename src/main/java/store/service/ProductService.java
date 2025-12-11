package store.service;

import store.domain.Product;
import store.repository.ProductRepository;

import java.util.List;

public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

   public List<Product> readProducts() {
        return productRepository.findAll();
    }
}
