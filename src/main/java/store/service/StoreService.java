package store.service;

public class StoreService {
    private final OrderService orderService;
    private final PromotionService promotionService;
    private final DiscountService discountService;

    public StoreService(OrderService orderService, PromotionService promotionService, DiscountService discountService) {
        this.orderService = orderService;
        this.promotionService = promotionService;
        this.discountService = discountService;
    }
}
