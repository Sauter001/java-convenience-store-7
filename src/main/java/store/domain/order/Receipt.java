package store.domain.order;

import store.domain.order.dto.OrderPresentedDto;
import store.domain.order.dto.OrderReceiptDto;

import java.util.List;

public record Receipt(List<OrderReceiptDto> purchasedItems,
                      List<OrderPresentedDto> presentedItems,
                      PaymentAmount paymentAmount) {

}
