package store.domain.order;

import store.domain.order.dto.OrderPresentedDto;
import store.domain.order.dto.OrderReceiptDto;

import java.util.List;

public record Receipt(List<OrderReceiptDto> purchasedItems,
                      List<OrderPresentedDto> presentedItems,
                      PaymentAmount paymentAmount) {
    public record PaymentAmount(int totalAmount,
                                int promotionDiscount,
                                int membershipDiscount) {
        public int calculateFinalAmount() {
            return totalAmount - promotionDiscount - membershipDiscount;
        }
    }

    public int totalQuantity() {
        return this.purchasedItems.stream().mapToInt(OrderReceiptDto::quantity).sum();
    }
}
