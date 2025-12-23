package store.domain.promotion;

public class BuyGetQuantity {
    private final int requiredQuantity; // 구매해야 할 수량
    private final int bonusQuantity;    // 증정 수량

    public BuyGetQuantity(int requiredQuantity, int bonusQuantity) {
        this.requiredQuantity = requiredQuantity;
        this.bonusQuantity = bonusQuantity;
    }
}
