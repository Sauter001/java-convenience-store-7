package store.parser.row;

import store.domain.io.Row;
import store.domain.promotion.BuyGetQuantity;
import store.domain.promotion.Promotion;
import store.domain.promotion.PromotionPeriod;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PromotionRowsParser implements RowParser<List<Promotion>> {
    private static final String COL_NAME = "name";
    private static final String COL_BUY = "buy";
    private static final String COL_GET = "get";
    private static final String COL_START_DATE = "start_date";
    private static final String COL_END_DATE = "end_date";
    private static final List<String> keyNames = List.of(COL_NAME, COL_BUY, COL_GET, COL_START_DATE, COL_END_DATE);

    @Override
    public List<Promotion> parse(List<Row> rows) {
        return rows.stream()
                .map(this::convertToPromotion)
                .toList();
    }

    private Promotion convertToPromotion(Row row) {
        Iterator<String> rowIterator = row.getColumns();
        Map<String, String> promotionMap = new HashMap<>();

        int keyIndex = 0;
        while (rowIterator.hasNext()) {
            String column = rowIterator.next();
            promotionMap.put(keyNames.get(keyIndex), column);
            keyIndex++;
        }
        return columnsToPromotion(promotionMap);
    }

    private Promotion columnsToPromotion(Map<String, String> promotionMap) {
        String name = promotionMap.get(COL_NAME);
        int buy = Integer.parseInt(promotionMap.get(COL_BUY));
        int get = Integer.parseInt(promotionMap.get(COL_GET));
        LocalDate startDate = LocalDate.parse(promotionMap.get(COL_START_DATE));
        LocalDate endDate = LocalDate.parse(promotionMap.get(COL_END_DATE));

        BuyGetQuantity buyGetQuantity = new BuyGetQuantity(buy, get);
        PromotionPeriod period = new PromotionPeriod(startDate, endDate);

        return new Promotion(name, buyGetQuantity, period);
    }
}
