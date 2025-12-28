package store.service;

import store.domain.order.Orders;
import store.domain.order.Receipt;
import store.domain.order.dto.OrderPresentedDto;
import store.domain.order.dto.OrderReceiptDto;

import java.util.List;

public class DiscountService {
    public Receipt createReceipt(Orders orders, boolean isMembership) {
        List<OrderReceiptDto> receiptDtos = orders.toReceiptDtos();
        List<OrderPresentedDto> presentedDtos = orders.toPresentedDtos();
        int membershipDiscount = calculateMembershipDiscount(orders, isMembership);
        Receipt.PaymentAmount paymentAmount = createPaymentAmount(orders, membershipDiscount);

        return new Receipt(receiptDtos, presentedDtos, paymentAmount);
    }

    private static Receipt.PaymentAmount createPaymentAmount(Orders orders, int membershipDiscount) {
        int fullAmount = orders.getFullAmount();
        int promotionDiscount = orders.getPromotionDiscount();
        return new Receipt.PaymentAmount(
                fullAmount,
                promotionDiscount,
                membershipDiscount
        );
    }

    private int calculateMembershipDiscount(Orders orders, boolean isMembership) {
        if (isMembership) {
            return orders.calculateMembershipDiscount();
        }
        return 0;
    }
}
