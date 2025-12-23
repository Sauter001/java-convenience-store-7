package store.domain.order;

public record PaymentAmount(int totalAmount,
                            int promotionDiscount,
                            int membershipDiscount) {
    public int calculateFinalAmount() {
        return totalAmount - promotionDiscount - membershipDiscount;
    }
}
