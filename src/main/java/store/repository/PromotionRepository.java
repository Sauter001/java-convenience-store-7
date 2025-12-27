package store.repository;

import store.constant.FileConstant;
import store.domain.io.Row;
import store.domain.promotion.Promotion;
import store.parser.row.PromotionRowsParser;
import store.tool.CSVReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PromotionRepository {
    private final Map<String, Promotion> promotions;

    public PromotionRepository() {
        this.promotions = loadPromotions();
    }

    private Map<String, Promotion> loadPromotions() {
        CSVReader csvReader = new CSVReader(FileConstant.PROMOTION_PATH);
        List<Row> rows = csvReader.readRows();
        PromotionRowsParser parser = new PromotionRowsParser();
        List<Promotion> promotionList = parser.parse(rows);

        Map<String, Promotion> promotionMap = new HashMap<>();
        for (Promotion promotion : promotionList) {
            promotionMap.put(promotion.getName(), promotion);
        }
        return promotionMap;
    }

    public Promotion findByName(String name) {
        return promotions.get(name);
    }
}
