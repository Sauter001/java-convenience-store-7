package store.domain.product;

public class Stock {
    private final int promotionStock;
    private final int normalStock;

    public Stock(int promotionStock, int normalStock) {
        this.promotionStock = promotionStock;
        this.normalStock = normalStock;
    }

    public int getPromotionStock() {
        return promotionStock;
    }

    public int getNormalStock() {
        return normalStock;
    }

    public int getSetSize() {
        return promotionStock + normalStock;
    }
}
