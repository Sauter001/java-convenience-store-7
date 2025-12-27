package store;

import org.junit.jupiter.api.Test;
import store.domain.io.Row;
import store.tool.CSVReader;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CSVReaderQuickTest {

    @Test
    void should_read_products_file() {
        CSVReader reader = new CSVReader("products.md");
        List<Row> rows = reader.readRows();

        assertThat(rows).isNotEmpty();
        System.out.println("파일 읽기 성공! 총 row 수: " + rows.size());
    }

    @Test
    void should_read_promotions_file() {
        CSVReader reader = new CSVReader("promotions.md");
        List<Row> rows = reader.readRows();

        assertThat(rows).isNotEmpty();
        System.out.println("파일 읽기 성공! 총 row 수: " + rows.size());
    }
}
