package store.domain.product;

import store.domain.product.dto.ProductDisplayDto;

import java.util.ArrayList;
import java.util.List;

public class Products {
    private final List<Product> products;

    public Products(List<Product> products) {
        this.products = products;
    }

    public List<ProductDisplayDto> toAllDisplayDtos() {
        List<ProductDisplayDto> dtos = new ArrayList<>();
        for (Product product : products) {
            dtos.addAll(product.toDisplayDtos());
        }
        return dtos;
    }
}
