package store.parser.input;

import store.domain.order.dto.OrderForm;
import store.exception.InvalidOrderFormatException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class OrderFormParser implements Parser<List<OrderForm>> {
    public static final String DELIMITER = ",";
    private static final Pattern orderPattern = Pattern.compile("\\[(.+)-(\\d+)\\]");

    @Override
    public List<OrderForm> parse(String input) {
        List<OrderForm> orders = new ArrayList<>();
        List<String> tokens = Stream.of(input.split(DELIMITER)).map(String::strip).toList();
        for (String token : tokens) {
            Matcher matcher = orderPattern.matcher(token);
            orders.add(makeOrder(matcher));
        }
        return orders;
    }

    private OrderForm makeOrder(Matcher matcher) {
        if (!matcher.find()) {
            throw new InvalidOrderFormatException();
        }

        String productName = matcher.group(1);
        int quantity = Integer.parseInt(matcher.group(2));
        return new OrderForm(productName, quantity);
    }
}
