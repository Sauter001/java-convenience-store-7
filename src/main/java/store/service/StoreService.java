package store.service;

import store.domain.product.Product;
import store.domain.product.Products;

import java.util.List;

public class StoreService {
    private final OrderService orderService;
    private final PromotionService promotionService;
    private final DiscountService discountService;

    public StoreService(OrderService orderService, PromotionService promotionService, DiscountService discountService) {
        this.orderService = orderService;
        this.promotionService = promotionService;
        this.discountService = discountService;
    }

    public Products findAllProducts() {
        return new Products(orderService.getProducts());
    }
}
