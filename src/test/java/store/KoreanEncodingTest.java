package store;

import org.junit.jupiter.api.Test;
import store.domain.io.Row;
import store.tool.CSVReader;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class KoreanEncodingTest {

    @Test
    void should_read_korean_characters_from_products_file() {
        CSVReader reader = new CSVReader("products.md");
        List<Row> rows = reader.readRows();

        assertThat(rows).isNotEmpty();

        // First row should contain "콜라"
        String firstProductName = rows.get(0).getColumns().next();
        System.out.println("First product name: " + firstProductName);
        assertThat(firstProductName).isEqualTo("콜라");
    }

    @Test
    void should_read_korean_characters_from_promotions_file() {
        CSVReader reader = new CSVReader("promotions.md");
        List<Row> rows = reader.readRows();

        assertThat(rows).isNotEmpty();

        // First row should contain "탄산2+1"
        String firstPromotionName = rows.get(0).getColumns().next();
        System.out.println("First promotion name: " + firstPromotionName);
        assertThat(firstPromotionName).isEqualTo("탄산2+1");
    }
}
