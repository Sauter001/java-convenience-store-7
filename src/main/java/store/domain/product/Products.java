package store.domain.product;

import store.domain.product.dto.ProductOverviewDto;

import java.util.List;

public class Products {
    private final List<Product> products;

    public Products(List<Product> products) {
        this.products = products;
    }

    public List<ProductOverviewDto> toOverviewDtos() {
        return this.products.stream()
                .flatMap(p -> p.toOverviewDtos().stream())
                .toList();
    }
}
