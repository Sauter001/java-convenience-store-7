package store.domain.order;

public record PaymentAmount(int totalAmount,
                            int promotionDiscount,
                            int membershipDiscount) {

}
