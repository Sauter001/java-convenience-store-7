package store.domain;

import store.domain.state.StockState;

import java.util.Optional;

public class StockTarget {
    private final StockState stockState;
    private final Order order;

    public StockTarget(StockState stockState, Order order) {
        this.stockState = stockState;
        this.order = order;
    }

    public boolean stackStateEquals(StockState stockState) {
        return this.stockState.equals(stockState);
    }
}
