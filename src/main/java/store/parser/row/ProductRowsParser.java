package store.parser.row;

import store.domain.io.Row;
import store.domain.product.ProductData;
import store.exception.ServiceException;

import java.util.*;

public class ProductRowsParser implements RowParser<List<ProductData>> {
    public static final String COL_NAME = "name";
    public static final String COL_PRICE = "price";
    public static final String COL_QUANTITY = "quantity";
    public static final String COL_PROMOTION = "promotion";
    private static final List<String> keyNames = List.of(COL_NAME, COL_PRICE, COL_QUANTITY, COL_PROMOTION);

    @Override
    public List<ProductData> parse(List<Row> rows) {
        List<ProductData> products = new ArrayList<>();
        for (Row row : rows) {
            products.add(convertToProductData(row));
        }
        return products;
    }

    private ProductData convertToProductData(Row row) {
        Iterator<String> rowIterator = row.getColumns();
        Map<String, String> productMap = new HashMap<>();

        int keyIndex = 0;
        while (rowIterator.hasNext()) {
            String column = rowIterator.next();
            productMap.put(keyNames.get(keyIndex), column);
            keyIndex++;
        }
        return columnsToProductData(productMap);
    }

    private ProductData columnsToProductData(Map<String, String> productMap) {
        String name = productMap.get(COL_NAME);
        int price = Integer.parseInt(productMap.get(COL_PRICE));
        int quantity = Integer.parseInt(productMap.get(COL_QUANTITY));
        String promotionName = productMap.get(COL_PROMOTION);

        validatePrice(price);
        validateQuantity(quantity);
        return new ProductData(name, price, quantity, promotionName);
    }

    private void validatePrice(int price) {
        if (price <= 0) {
            throw new ServiceException("가격은 0보다 커야 합니다.");
        }
    }

    private void validateQuantity(int quantity) {
        if (quantity < 0) {
            throw new ServiceException("수량은 0 이상이어야 합니다.");
        }
    }
}
