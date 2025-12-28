package store.domain.product;

import store.domain.product.dto.ProductDisplayDto;

import java.util.Iterator;
import java.util.List;

public class Products implements Iterable<Product> {
    private final List<Product> products;

    public Products(List<Product> products) {
        this.products = products;
    }

    public List<ProductDisplayDto> toAllDisplayDtos() {
        return this.products.stream()
                .flatMap(p -> p.toDisplayDtos().stream())
                .toList();
    }

    @Override
    public Iterator<Product> iterator() {
        return this.products.iterator();
    }
}
