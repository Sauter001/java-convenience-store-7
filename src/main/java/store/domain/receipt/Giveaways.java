package store.domain.receipt;

import java.util.List;

public class Giveaways {
    private final List<Giveaway> giveaways;

    public Giveaways(List<Giveaway> giveaways) {
        this.giveaways = giveaways;
    }

    public int getPromotionDiscount() {
        return this.giveaways.stream()
                .mapToInt(Giveaway::discount)
                .sum();
    }
}
