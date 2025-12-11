package store.parser;

import store.domain.Promotion;
import store.domain.dao.PromotionDAO;

import java.util.Arrays;
import java.util.List;

public class PromotionCSVParser {
    private static final String DELIMITER = ",";

    public static PromotionDAO parse(String line) {
        List<String> parts = Arrays.stream(line.split(DELIMITER)).map(String::strip).toList();
        Promotion promotion = Promotion.getPromotionFrom(parts.get(0));
        int toBuy = Integer.parseInt(parts.get(1));
        int toFree = Integer.parseInt(parts.get(2));

        return new PromotionDAO(promotion, toBuy, toFree);
    }
}
