package store.domain.product;

public class Stock {
    private int normalQuantity;
    private int promotionQuantity;

    public Stock(int normalQuantity, int promotionQuantity) {
        this.normalQuantity = normalQuantity;
        this.promotionQuantity = promotionQuantity;
    }
}
