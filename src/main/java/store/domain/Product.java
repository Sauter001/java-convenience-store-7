package store.domain;

public record Product(String name, int price, int stock, Promotion promotion) {
    public static Product fromPromotionName(String productName, int price, int stock, String promotionName) {
        return new Product(productName, price, stock, Promotion.getPromotionFrom(promotionName));
    }

    public boolean hasPromotion() {
        return this.promotion != Promotion.NONE;
    }

    public String getPromotionName() {
        return promotion.getName();
    }
}
