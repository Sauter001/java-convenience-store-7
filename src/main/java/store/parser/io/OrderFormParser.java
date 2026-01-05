package store.parser.io;

import store.domain.order.dto.OrderForm;
import store.error.InvalidOrderException;
import store.error.StoreException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class OrderFormParser implements Parser<List<OrderForm>> {
    private static final Pattern orderPattern = Pattern.compile("\\[(.+)-(\\d+)]");
    private static final String DELIMITER = ",";

    @Override
    public List<OrderForm> parse(String input) {
        List<String> tokens = Stream.of(input.split(DELIMITER))
                .map(String::strip)
                .toList();
        return createOrderForms(tokens);
    }

    private List<OrderForm> createOrderForms(List<String> tokens) {
        try {
            List<OrderForm> orderForms = new ArrayList<>();
            for (String token : tokens) {
                orderForms.add(createForm(token));
            }
            return orderForms;
        } catch (StoreException e) {
            throw new InvalidOrderException();
        }
    }

    private OrderForm createForm(String token) {
        Matcher matcher = orderPattern.matcher(token);
        if (!matcher.find()) {
            throw new InvalidOrderException();
        }
        String productName = matcher.group(1);
        int quantity = Integer.parseInt(matcher.group(2));
        return new OrderForm(productName, quantity);
    }
}
