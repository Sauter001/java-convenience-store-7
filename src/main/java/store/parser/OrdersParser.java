package store.parser;

import store.domain.Order;
import store.exception.ErrorMessage;
import store.exception.ServiceException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrdersParser implements DomainParser<List<Order>> {
    private static final String DELIMITER = ",";
    private static final Pattern ORDER_PATTERN = Pattern.compile("\\[(.+)-(\\d+)\\]");

    private static Order convertToOrder(Matcher matcher) {
        if (!matcher.find()) {
            throw new ServiceException(ErrorMessage.INVALID_INPUT_FORMAT);
        }

        String productName = matcher.group(1);
        int quantity = Integer.parseInt(matcher.group(2));
        return new Order(productName, quantity);
    }

    private static List<Order> addAllOrder(List<Matcher> matchers) {
        List<Order> orders = new ArrayList<>();

        for (Matcher matcher : matchers) {
            Order order = convertToOrder(matcher);
            orders.add(order);
        }

        return orders;
    }

    @Override
    public List<Order> parse(String input) {
        List<String> columns = Arrays.stream(input.split(DELIMITER))
                .map(String::strip)
                .toList();
        List<Matcher> matchers = columns.stream().map(ORDER_PATTERN::matcher).toList();
        return addAllOrder(matchers);
    }
}
