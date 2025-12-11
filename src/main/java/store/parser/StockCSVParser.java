package store.parser;

import store.domain.Product;
import store.domain.Promotion;

import java.util.Arrays;
import java.util.List;

public class StockCSVParser {

    private static final String DELIMITER = ",";
    public static final int PRODUCT_NAME_COL = 0;
    public static final int PRICE_COL = 1;
    public static final int QUANTITY_COL = 2;
    public static final int PROMOTION_COL = 3;

    public static Product parseProduct(String line) {
        List<String> parts = Arrays.asList(line.split(DELIMITER));
        String name = parts.get(PRODUCT_NAME_COL);
        int price =  Integer.parseInt(parts.get(PRICE_COL));
        int quantity = Integer.parseInt(parts.get(QUANTITY_COL));
        Promotion promotion = Promotion.getPromotionFrom(parts.get(PROMOTION_COL));

        return new Product(name, price, quantity, promotion);
    }
}
