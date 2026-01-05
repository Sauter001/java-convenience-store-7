package store.parser.row;

import store.domain.io.Row;
import store.domain.io.Rows;
import store.domain.product.Product;
import store.domain.product.Stock;

import java.util.*;

public class ProductRowsParser implements RowsParser<List<Product>> {
    private static final int SOLD_OUT = 0;
    private final Map<StockKey, String> promotionNameMap = new HashMap<>();
    private final Map<StockKey, Integer> promotionStockMap = new HashMap<>();
    private final Map<StockKey, Integer> normalStockMap = new HashMap<>();

    @Override
    public List<Product> parse(Rows rows) {
        Set<StockKey> stockKeys = new TreeSet<>();
        for (Row row : rows) {
            StockKey stockKey = createStockKey(row);
            putStockByPromotion(stockKey, row, hasPromotion(row));
            putPromotion(row, stockKey);
            stockKeys.add(stockKey);
        }
        return compositeProduct(stockKeys);
    }

    private void putPromotion(Row row, StockKey stockKey) {
        if (!hasPromotion(row)) {
            return;
        }

        promotionNameMap.put(stockKey, row.contentOf(Column.PROMOTION.order));
    }

    private List<Product> compositeProduct(Set<StockKey> stockKeys) {
        List<Product> products = new ArrayList<>();
        for (StockKey key : stockKeys) {
            Stock stock = new Stock(
                    promotionStockMap.getOrDefault(key, SOLD_OUT),
                    normalStockMap.getOrDefault(key, SOLD_OUT));
            products.add(createProduct(key, stock));
        }
        return products;
    }

    private Product createProduct(StockKey key, Stock stock) {
        return new Product(
                key.name,
                key.price,
                stock,
                promotionNameMap.get(key)
        );
    }

    private void putStockByPromotion(StockKey stockKey, Row row, boolean isPromotion) {
        int stock = Integer.parseInt(row.contentOf(Column.QUANTITY.order));
        if (isPromotion) {
            promotionStockMap.put(stockKey, stock);
            return;
        }
        normalStockMap.put(stockKey, stock);
    }

    private boolean hasPromotion(Row row) {
        String promotionContent = row.contentOf(Column.PROMOTION.order);;
        String nullContent = "null";
        return Objects.nonNull(promotionContent) && !nullContent.equals(promotionContent);
    }

    private StockKey createStockKey(Row row) {
        int price = Integer.parseInt(row.contentOf(Column.PRICE.order));
        return new StockKey(row.contentOf(Column.NAME.order),
                price);
    }

    private String getPromotionType(Row row) {
        if (!hasPromotion(row)) {
            return null;
        }
        return row.contentOf(Column.PROMOTION.order);
    }

    private enum Column {
        NAME(0), PRICE(1), QUANTITY(2), PROMOTION(3);

        private final int order;

        Column(int order) {
            this.order = order;
        }
    }

    private record StockKey(String name, int price) implements Comparable<StockKey> {
        @Override
        public int compareTo(StockKey key) {
            return name.compareTo(key.name);
        }
    }
}
