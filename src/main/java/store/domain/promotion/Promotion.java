package store.domain.promotion;

import java.util.Objects;

public class Promotion {
    private final String name;
    private final BuyGet buyGet ;
    private final Period period;

    public Promotion(String name, BuyGet buyGet, Period period) {
        this.name = name;
        this.buyGet = buyGet;
        this.period = period;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Promotion promotion)) {
            return false;
        }
        return Objects.equals(name, promotion.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
