package store.parser.row;

import org.junit.jupiter.api.Test;
import store.domain.io.Row;
import store.domain.promotion.Promotion;
import store.exception.ServiceException;

import java.time.format.DateTimeParseException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PromotionRowsParserTest {
    private final PromotionRowsParser parser = new PromotionRowsParser();

    @Test
    void should_parse_buy_two_get_one_promotion() {
        Row row = new Row(List.of("탄산2+1", "2", "1", "2025-01-01", "2025-12-31"));

        List<Promotion> result = parser.parse(List.of(row));

        assertThat(result).hasSize(1);
        Promotion promotion = result.get(0);
        assertThat(promotion.getName()).isEqualTo("탄산2+1");
    }

    @Test
    void should_parse_buy_one_get_one_promotion() {
        Row row = new Row(List.of("MD추천상품", "1", "1", "2025-01-01", "2025-12-31"));

        List<Promotion> result = parser.parse(List.of(row));

        assertThat(result).hasSize(1);
        Promotion promotion = result.get(0);
        assertThat(promotion.getName()).isEqualTo("MD추천상품");
    }

    @Test
    void should_parse_multiple_promotions() {
        Row row1 = new Row(List.of("탄산2+1", "2", "1", "2025-01-01", "2025-12-31"));
        Row row2 = new Row(List.of("MD추천상품", "1", "1", "2025-01-01", "2025-12-31"));

        List<Promotion> result = parser.parse(List.of(row1, row2));

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("탄산2+1");
        assertThat(result.get(1).getName()).isEqualTo("MD추천상품");
    }

    @Test
    void should_parse_promotion_successfully() {
        Row row = new Row(List.of("반짝할인", "3", "2", "2025-06-01", "2025-06-30"));

        List<Promotion> result = parser.parse(List.of(row));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("반짝할인");
    }

    @Test
    void should_throw_exception_when_insufficient_columns() {
        Row row = new Row(List.of("탄산2+1", "2"));

        assertThatThrownBy(() -> parser.parse(List.of(row)))
                .isInstanceOf(NumberFormatException.class);
    }

    @Test
    void should_throw_exception_when_buy_not_number() {
        Row row = new Row(List.of("탄산2+1", "two", "1", "2025-01-01", "2025-12-31"));

        assertThatThrownBy(() -> parser.parse(List.of(row)))
                .isInstanceOf(NumberFormatException.class);
    }

    @Test
    void should_throw_exception_when_get_not_number() {
        Row row = new Row(List.of("탄산2+1", "2", "one", "2025-01-01", "2025-12-31"));

        assertThatThrownBy(() -> parser.parse(List.of(row)))
                .isInstanceOf(NumberFormatException.class);
    }

    @Test
    void should_throw_exception_when_invalid_date_format() {
        Row row = new Row(List.of("탄산2+1", "2", "1", "2025/01/01", "2025-12-31"));

        assertThatThrownBy(() -> parser.parse(List.of(row)))
                .isInstanceOf(DateTimeParseException.class);
    }

    @Test
    void should_throw_exception_when_invalid_date() {
        Row row = new Row(List.of("탄산2+1", "2", "1", "2025-13-01", "2025-12-31"));

        assertThatThrownBy(() -> parser.parse(List.of(row)))
                .isInstanceOf(DateTimeParseException.class);
    }

    @Test
    void should_throw_exception_when_empty_row() {
        Row row = new Row(List.of());

        assertThatThrownBy(() -> parser.parse(List.of(row)))
                .isInstanceOf(NumberFormatException.class);
    }

    @Test
    void should_throw_exception_when_negative_buy() {
        Row row = new Row(List.of("탄산2+1", "-2", "1", "2025-01-01", "2025-12-31"));

        assertThatThrownBy(() -> parser.parse(List.of(row)))
                .isInstanceOf(ServiceException.class);
    }

    @Test
    void should_throw_exception_when_negative_get() {
        Row row = new Row(List.of("탄산2+1", "2", "-1", "2025-01-01", "2025-12-31"));

        assertThatThrownBy(() -> parser.parse(List.of(row)))
                .isInstanceOf(ServiceException.class);
    }
}
