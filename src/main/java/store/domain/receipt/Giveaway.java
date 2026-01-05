package store.domain.receipt;

public record Giveaway(String productName, int quantity, int unitPrice) {
    public int discount() {
        return quantity * unitPrice;
    }
}
