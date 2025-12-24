package store.domain.promotion;

public class Promotion {
    private final String name;
    private final BuyGetQuantity buyGetQuantity;
    private final PromotionPeriod period;

    public Promotion(String name, BuyGetQuantity buyGetQuantity, PromotionPeriod period) {
        this.name = name;
        this.buyGetQuantity = buyGetQuantity;
        this.period = period;
    }

    public String getName() {
        return name;
    }

    public boolean isExpired() {
        return period.isExpired();
    }
}
