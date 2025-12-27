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

    public StockState checkStockState(int quantity) {
        if (promotionStock >= quantity) {
            return StockState.PROMOTION_ONLY;
        }
        if (promotionStock + normalStock >= quantity) {
            return StockState.PROMOTION_WITH_NORMAL;
        }
        return StockState.INSUFFICIENT;
    }
}
