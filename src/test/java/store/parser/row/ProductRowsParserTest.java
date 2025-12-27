package store.parser.row;

import org.junit.jupiter.api.Test;
import store.domain.io.Row;
import store.domain.product.ProductData;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductRowsParserTest {
    private final ProductRowsParser parser = new ProductRowsParser();

    @Test
    void should_parse_product_with_promotion() {
        Row row = new Row(List.of("콜라", "1000", "10", "탄산2+1"));

        List<ProductData> result = parser.parse(List.of(row));

        assertThat(result).hasSize(1);
        ProductData productData = result.getFirst();
        assertThat(productData.name()).isEqualTo("콜라");
        assertThat(productData.price()).isEqualTo(1000);
        assertThat(productData.quantity()).isEqualTo(10);
        assertThat(productData.promotionName()).isEqualTo("탄산2+1");
    }

    @Test
    void should_parse_product_without_promotion() {
        Row row = new Row(List.of("콜라", "1000", "10", "null"));

        List<ProductData> result = parser.parse(List.of(row));

        assertThat(result).hasSize(1);
        ProductData productData = result.getFirst();
        assertThat(productData.name()).isEqualTo("콜라");
        assertThat(productData.price()).isEqualTo(1000);
        assertThat(productData.quantity()).isEqualTo(10);
        assertThat(productData.promotionName()).isEqualTo("null");
    }

    @Test
    void should_parse_multiple_rows() {
        Row row1 = new Row(List.of("콜라", "1000", "10", "탄산2+1"));
        Row row2 = new Row(List.of("사이다", "1200", "5", "null"));

        List<ProductData> result = parser.parse(List.of(row1, row2));

        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("콜라");
        assertThat(result.get(1).name()).isEqualTo("사이다");
    }

    @Test
    void should_parse_all_fields_correctly() {
        Row row = new Row(List.of("에너지바", "2000", "15", "MD추천상품"));

        List<ProductData> result = parser.parse(List.of(row));

        assertThat(result).hasSize(1);
        ProductData productData = result.getFirst();
        assertThat(productData.name()).isEqualTo("에너지바");
        assertThat(productData.price()).isEqualTo(2000);
        assertThat(productData.quantity()).isEqualTo(15);
        assertThat(productData.promotionName()).isEqualTo("MD추천상품");
    }

    @Test
    void should_throw_exception_when_insufficient_columns() {
        Row row = new Row(List.of("콜라", "1000"));

        assertThatThrownBy(() -> parser.parse(List.of(row)))
                .isInstanceOf(NumberFormatException.class);
    }

    @Test
    void should_throw_exception_when_price_not_number() {
        Row row = new Row(List.of("콜라", "천원", "10", "null"));

        assertThatThrownBy(() -> parser.parse(List.of(row)))
                .isInstanceOf(NumberFormatException.class);
    }

    @Test
    void should_throw_exception_when_quantity_not_number() {
        Row row = new Row(List.of("콜라", "1000", "열개", "null"));

        assertThatThrownBy(() -> parser.parse(List.of(row)))
                .isInstanceOf(NumberFormatException.class);
    }

    @Test
    void should_throw_exception_when_empty_row() {
        Row row = new Row(List.of());

        assertThatThrownBy(() -> parser.parse(List.of(row)))
                .isInstanceOf(NumberFormatException.class);
    }

    @Test
    void should_throw_exception_when_negative_price() {
        Row row = new Row(List.of("콜라", "-1000", "10", "null"));

        assertThatThrownBy(() -> parser.parse(List.of(row)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void should_throw_exception_when_negative_quantity() {
        Row row = new Row(List.of("콜라", "1000", "-10", "null"));

        assertThatThrownBy(() -> parser.parse(List.of(row)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
