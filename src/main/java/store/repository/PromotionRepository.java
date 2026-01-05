package store.repository;

import store.constant.IOConstant;
import store.domain.io.Row;
import store.domain.io.Rows;
import store.domain.promotion.Promotion;
import store.error.StoreException;
import store.parser.io.CsvRowParser;
import store.parser.row.PromotionRowsParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class PromotionRepository {
    private final Set<Promotion> promotions;

    public PromotionRepository() {
        this.promotions = readPromotions();
    }

    private Set<Promotion> readPromotions() {
        try (InputStream is = getClass().getResourceAsStream(IOConstant.PROMOTION_PATH);
             InputStreamReader isr = new InputStreamReader(Objects.requireNonNull(is));
             BufferedReader reader = new BufferedReader(isr)) {
            return readPromotionsWith(reader);
        } catch (IOException e) {
            throw new StoreException("프로모션 파일 읽기 실패");
        }
    }

    private Set<Promotion> readPromotionsWith(BufferedReader reader) throws IOException {
        List<Row> rowsContent = new ArrayList<>();
        CsvRowParser csvRowParser = new CsvRowParser();
        PromotionRowsParser promotionRowsParser = new PromotionRowsParser();
        while (reader.ready()) {
            String line = reader.readLine();
            rowsContent.add(csvRowParser.parse(line));
        }
        return promotionRowsParser.parse(new Rows(rowsContent));
    }
}
