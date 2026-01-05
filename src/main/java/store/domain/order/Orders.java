package store.domain.order;

import java.util.List;

public class Orders {
    private final List<Order> orders;

    public Orders(List<Order> orders) {
        this.orders = orders;
    }

    public int getFullPriceOfAllProducts() {
        return this.orders.stream()
                .mapToInt(Order::getFullPrice)
                .sum();
    }

    public boolean isEmpty() {
        return this.orders.isEmpty();
    }
}
