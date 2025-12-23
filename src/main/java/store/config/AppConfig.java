package store.config;

import store.domain.repository.ProductRepository;
import store.domain.repository.PromotionRepository;
import store.service.DiscountService;
import store.service.OrderService;
import store.service.PromotionService;
import store.service.StoreService;

public class AppConfig {
    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;

    public AppConfig() {
        this.productRepository = new ProductRepository();
        this.promotionRepository = new PromotionRepository();
    }

    private OrderService createOrderService() {
        return new OrderService(productRepository);
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
