package store.config;

import store.repository.ProductRepository;
import store.repository.PromotionRepository;
import store.service.DiscountService;
import store.service.OrderService;
import store.service.PromotionService;
import store.service.StoreService;

public class AppConfig {
    private final PromotionRepository promotionRepository;
    private final ProductRepository productRepository;

    public AppConfig() {
        this.promotionRepository = new PromotionRepository();
        this.productRepository = new ProductRepository();
    }

    private OrderService createOrderService() {
        return new OrderService(productRepository, promotionRepository);
    }

    private PromotionService createPromotionService() {
        return new PromotionService(promotionRepository);
    }

    private DiscountService createDiscountService() {
        return new DiscountService();
    }

    public StoreService createStoreService() {
        return new StoreService(
                createOrderService(),
                createPromotionService(),
                createDiscountService()
        );
    }
}
