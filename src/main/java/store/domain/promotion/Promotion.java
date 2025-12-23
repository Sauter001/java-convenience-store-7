package store.domain.promotion;

import java.time.LocalDate;

public class Promotion {
    private final String name;
    private final BuyGetQuantity buyGetQuantity;
    private final PromotionPeriod period;


    public Promotion(String name, BuyGetQuantity buyGetQuantity,  PromotionPeriod period) {
        this.name = name;
        this.buyGetQuantity = buyGetQuantity;
        this.period = period;
    }
}
