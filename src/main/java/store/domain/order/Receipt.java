package store.domain.order;

import java.util.List;

public record Receipt(List<Order> purchasedItems,
                      List<Order> presentedItems,
                      PaymentAmount paymentAmount) {

}
