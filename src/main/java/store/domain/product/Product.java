package store.domain.product;

import store.domain.product.dto.ProductOverviewDto;

import java.util.List;
import java.util.Objects;

public class Product {
    private final String name;
    private final int price;
    private final Stock stock;
    private final String promotionType;

    public Product(String name, int price, Stock stock, String promotionType) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.promotionType = promotionType;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Product product)) {
            return false;
        }
        return Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    public boolean hasPromotion() {
        return Objects.nonNull(promotionType);
    }

    public List<ProductOverviewDto> toOverviewDtos() {
        if (hasPromotion()) {
            return List.of(
                    new ProductOverviewDto(this.name, this.price, this.stock.getPromotionStock(), this.promotionType),
                    new ProductOverviewDto(this.name, this.price, this.stock.getNormalStock(), null));
        }

        return List.of(new ProductOverviewDto(this.name, this.price, this.stock.getNormalStock(), null));
    }
}
