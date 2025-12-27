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

    public int getSetSize() {
        return buyGetQuantity.getSetSize();
    }

    public int getRequiredQuantity() {
        return buyGetQuantity.requiredQuantity();
    }

    public int getBonusQuantity() {
        return buyGetQuantity.bonusQuantity();
    }

    public boolean shouldSuggestAdditional(int requestedQuantity, int availablePromotionStock) {
        int remainder = requestedQuantity % getSetSize();
        return remainder == getRequiredQuantity()
                && availablePromotionStock >= requestedQuantity + getBonusQuantity();
    }
}
