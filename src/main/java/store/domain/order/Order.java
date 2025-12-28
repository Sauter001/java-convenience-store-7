package store.domain.order;

import store.domain.order.dto.PromotionConfirmation;
import store.domain.product.Product;
import store.domain.product.StockState;

import java.util.List;

public class Order {
    private final Product product;
    private int quantity;

    public Order(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public String getProductName() {
        return product.getName();
    }

    public PromotionConfirmation getPromotionConfirmation() {
        return product.getPromotionConfirmation(quantity);
    }

    public boolean shouldSuggestAdditionalItem() {
        return product.shouldSuggestAdditionalItem(quantity);
    }

    public int getAdditionalQuantity() {
        return product.getAdditionalQuantity(quantity);
    }

    public void adjustQuantity(int newQuantity) {
        this.quantity = newQuantity;
    }

    public void increaseQuantity(int additionalQuantity) {
        this.quantity += additionalQuantity;
    }

    public int getFullAmount() {
        return this.product.getPrice() * this.quantity;
    }
}
