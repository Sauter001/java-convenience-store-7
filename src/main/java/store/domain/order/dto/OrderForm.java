package store.domain.order.dto;

import store.error.InvalidOrderException;

public record OrderForm(String productName, int quantity) {
    public OrderForm {
        validateQuantity(quantity);
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new InvalidOrderException();
        }
    }
}
