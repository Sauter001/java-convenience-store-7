package store.domain.order;

import store.domain.order.dto.PromotionConfirmation;
import store.domain.product.Product;
import store.domain.product.StockState;

import java.util.List;

public class Order {
    private final Product product;
    private final int quantity;

    public Order(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public PromotionConfirmation getPromotionConfirmation() {
        return product.getPromotionConfirmation(quantity);
    }
}
