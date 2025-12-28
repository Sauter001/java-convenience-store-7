package store.domain.order;

import store.domain.order.dto.PromotionConfirmation;

import java.util.Iterator;
import java.util.List;

public class Orders implements Iterable<Order> {
    private final List<Order> orders;

    public Orders(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public Iterator<Order> iterator() {
        return orders.iterator();
    }

    public int calculateMembershipDiscount() {
        List<PromotionConfirmation> promotionConfirmations = this.orders.stream()
                .map(Order::getPromotionConfirmation)
                .toList();
    }

    public int getFullAmount() {
        return this.orders.stream()
                .mapToInt(Order::getFullAmount)
                .sum();
    }
}
