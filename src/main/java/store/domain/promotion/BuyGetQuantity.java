package store.domain.promotion;

public record BuyGetQuantity(int requiredQuantity, int bonusQuantity) {
    public int getSetSize() {
        return requiredQuantity + bonusQuantity;
    }
}
