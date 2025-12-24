package store.repository;

public class StoreRepository {
    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;

    public StoreRepository(ProductRepository productRepository,  PromotionRepository promotionRepository) {
        this.productRepository = productRepository;
        this.promotionRepository = promotionRepository;
    }
}
