package store.domain.order;

import store.domain.order.dto.OrderReceiptDto;

import java.util.Iterator;
import java.util.List;

public class Orders implements Iterable<Order> {
    public static final double MEMBERSHIP_DISCOUNT_RATE = 0.3;
    public static final int DISCOUNT_LIMIT_AMOUNT = 8000;
    private final List<Order> orders;

    public Orders(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public Iterator<Order> iterator() {
        return orders.iterator();
    }

    public int calculateMembershipDiscount() {
        int totalDiscountable = this.orders.stream()
                .mapToInt(Order::getDiscountableAmount)
                .sum();
        int discount = (int) Math.round(totalDiscountable * MEMBERSHIP_DISCOUNT_RATE);
        return Math.min(discount, DISCOUNT_LIMIT_AMOUNT);
    }

    public int getFullAmount() {
        return this.orders.stream()
                .mapToInt(Order::getFullAmount)
                .sum();
    }

    public List<OrderReceiptDto> toReceiptDtos() {
        return this.orders.stream()
                .map(Order::toReceiptDto)
                .toList();
    }
}
