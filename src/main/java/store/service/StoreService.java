package store.service;

import store.domain.product.Products;
import store.repository.ProductRepository;
import store.repository.PromotionRepository;

public class StoreService {
    private final PromotionRepository promotionRepository;
    private final ProductRepository productRepository;

    public StoreService(PromotionRepository promotionRepository, ProductRepository productRepository) {
        this.promotionRepository = promotionRepository;
        this.productRepository = productRepository;
    }

    public Products readProducts() {
        return new Products(productRepository.findAll());
    }
}
