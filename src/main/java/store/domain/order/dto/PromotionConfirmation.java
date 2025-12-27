package store.domain.order.dto;

public interface PromotionConfirmation {
    record FullyApplicable(String productName, int totalQuantity) implements PromotionConfirmation {
    }

    record PartiallyApplicable(String productName,
                               int promotionQuantity,
                               int regularPriceQuantity) implements PromotionConfirmation {
    }
}