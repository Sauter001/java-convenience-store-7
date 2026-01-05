package store.domain.promotion;

import java.util.List;
import java.util.Set;

public class Promotions {
    private final Set<Promotion> promotions;

    public Promotions(Set<Promotion> promotions) {
        this.promotions = promotions;
    }
}
