package store.domain.order;

import store.domain.order.dto.PromotionConfirmation;
import store.domain.product.Product;

public class Order {
    public static final int NO_DISCOUNT_COST = 0;
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

    public int getDiscountableAmount() {
        return getFullAmount() - getPromotionDiscount();
    }

    public int getFullAmount() {
        return this.product.calculateFullAmount(this.quantity);
    }

    public int getPromotionDiscount() {
        PromotionConfirmation confirmation = getPromotionConfirmation();
        if (confirmation instanceof PromotionConfirmation.FullyApplicable fullyApplicable) {
            return calculateDiscount(fullyApplicable);
        }
        if (confirmation instanceof PromotionConfirmation.PartiallyApplicable partiallyApplicable) {
            return calculateDiscount(partiallyApplicable);
        }
        return NO_DISCOUNT_COST;
    }

    private int calculateDiscount(PromotionConfirmation.PartiallyApplicable partial) {
        return this.product.calculatePromotionDiscount(partial.promotionQuantity());
    }

    private int calculateDiscount(PromotionConfirmation.FullyApplicable full) {
        return this.product.calculatePromotionDiscount(full.totalQuantity());
    }
}
