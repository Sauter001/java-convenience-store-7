package store.parser.input;

import org.junit.jupiter.api.Test;
import store.domain.order.dto.OrderForm;
import store.exception.InvalidOrderFormatException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderFormParserTest {
    private final OrderFormParser parser = new OrderFormParser();

    @Test
    void should_parse_single_product() {
        String input = "[콜라-2]";

        List<OrderForm> result = parser.parse(input);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(new OrderForm("콜라", 2));
    }

    @Test
    void should_parse_multiple_products() {
        String input = "[콜라-2],[사이다-3]";

        List<OrderForm> result = parser.parse(input);

        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isEqualTo(new OrderForm("콜라", 2));
        assertThat(result.get(1)).isEqualTo(new OrderForm("사이다", 3));
    }

    @Test
    void should_parse_with_whitespace() {
        String input = "[콜라-2], [사이다-3]";

        List<OrderForm> result = parser.parse(input);

        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isEqualTo(new OrderForm("콜라", 2));
        assertThat(result.get(1)).isEqualTo(new OrderForm("사이다", 3));
    }

    @Test
    void should_parse_large_quantity() {
        String input = "[콜라-100]";

        List<OrderForm> result = parser.parse(input);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(new OrderForm("콜라", 100));
    }

    @Test
    void should_throw_exception_when_no_brackets() {
        String input = "콜라-2";

        assertThatThrownBy(() -> parser.parse(input))
                .isInstanceOf(InvalidOrderFormatException.class);
    }

    @Test
    void should_throw_exception_when_no_hyphen() {
        String input = "[콜라2]";

        assertThatThrownBy(() -> parser.parse(input))
                .isInstanceOf(InvalidOrderFormatException.class);
    }

    @Test
    void should_throw_exception_when_no_quantity() {
        String input = "[콜라-]";

        assertThatThrownBy(() -> parser.parse(input))
                .isInstanceOf(InvalidOrderFormatException.class);
    }

    @Test
    void should_throw_exception_when_quantity_not_number() {
        String input = "[콜라-abc]";

        assertThatThrownBy(() -> parser.parse(input))
                .isInstanceOf(InvalidOrderFormatException.class);
    }

    @Test
    void should_throw_exception_when_empty_brackets() {
        String input = "[]";

        assertThatThrownBy(() -> parser.parse(input))
                .isInstanceOf(InvalidOrderFormatException.class);
    }

    @Test
    void should_throw_exception_when_invalid_input() {
        String input = "asdf";

        assertThatThrownBy(() -> parser.parse(input))
                .isInstanceOf(InvalidOrderFormatException.class);
    }

    @Test
    void should_throw_exception_when_special_characters() {
        String input = "@#$%";

        assertThatThrownBy(() -> parser.parse(input))
                .isInstanceOf(InvalidOrderFormatException.class);
    }

    @Test
    void should_throw_exception_when_single_open_bracket() {
        String input = "[";

        assertThatThrownBy(() -> parser.parse(input))
                .isInstanceOf(InvalidOrderFormatException.class);
    }

    @Test
    void should_throw_exception_when_single_close_bracket() {
        String input = "]";

        assertThatThrownBy(() -> parser.parse(input))
                .isInstanceOf(InvalidOrderFormatException.class);
    }

    @Test
    void should_throw_exception_when_only_hyphen() {
        String input = "[-]";

        assertThatThrownBy(() -> parser.parse(input))
                .isInstanceOf(InvalidOrderFormatException.class);
    }

    @Test
    void should_parse_product_name_ending_with_hyphen() {
        String input = "[콜라--1]";

        List<OrderForm> result = parser.parse(input);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(new OrderForm("콜라-", 1));
    }

    @Test
    void should_parse_zero_quantity() {
        String input = "[콜라-0]";

        List<OrderForm> result = parser.parse(input);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(new OrderForm("콜라", 0));
    }

    @Test
    void should_parse_product_name_with_hyphen() {
        String input = "[콜라-2-3]";

        List<OrderForm> result = parser.parse(input);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(new OrderForm("콜라-2", 3));
    }
}
