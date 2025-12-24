package store.domain.product;

public record ProductData(
        String name,
        int price,
        int quantity,
        String promotionName
) {
}
