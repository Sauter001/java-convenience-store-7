package store.domain;

import java.util.Arrays;

public enum Promotion {
    SODA("탄산2+1"),
    MD_RECOMMENDATION("MD추천상품"),
    FLASH_DISCOUNT("반짝할인"),
    NONE("");

    private final String name;

    Promotion(String name) {
        this.name = name;
    }

    public static Promotion getPromotionFrom(String name) {
        return Arrays.stream(values())
                .filter(p -> p.name.equals(name))
                .findFirst()
                .orElse(NONE);
    }

    public String getName() {
        return name;
    }
}
