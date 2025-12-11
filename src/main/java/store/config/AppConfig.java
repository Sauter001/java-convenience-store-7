package store.config;

import store.repository.ProductRepository;
import store.service.ProductService;

public class AppConfig {
    private final ProductRepository productRepository;

    public AppConfig() {
        this.productRepository = new ProductRepository();
    }

    public ProductService createProductService() {
        return new ProductService(productRepository);
    }
}
