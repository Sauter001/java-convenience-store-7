package store.domain.order;

import store.domain.order.dto.PromotionConfirmation;

import java.util.ArrayList;
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

    public List<PromotionConfirmation> getConfirmations() {
        return this.orders.stream()
                .map(Order::getPromotionConfirmation)
                .toList();
    }
}
