package store.domain.promotion;

public record BuyGet(int buy, int get) {
    public int setSize() {
        return buy + get;
    }
}
