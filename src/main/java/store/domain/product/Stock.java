package store.domain.product;

public class Stock {
    private int normalStock;
    private int promotionStock;

    public Stock(int normalStock, int promotionStock) {
        this.normalStock = normalStock;
        this.promotionStock = promotionStock;
    }

    public int getNormalStock() {
        return normalStock;
    }

    public int getPromotionStock() {
        return promotionStock;
    }
}
