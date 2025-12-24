package store.domain.product;

import store.domain.promotion.Promotion;

public class Product {
    private final String name;
    private final int price;
    private final Stock stock;
    private final Promotion promotion;

    public Product(String name, int price,  Stock stock) {
        this(name , price, stock, null);
    }

    public Product(String name, int price,  Stock stock,  Promotion promotion) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.promotion = promotion;
    }

    public boolean hasPromotion() {
        return promotion != null && !promotion.isExpired();
    }
}
