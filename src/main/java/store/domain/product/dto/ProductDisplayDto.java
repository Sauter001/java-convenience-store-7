package store.domain.product.dto;

public record ProductDisplayDto(String productName,
                                int price,
                                int stockQuantity,
                                String promotionName) {
}
