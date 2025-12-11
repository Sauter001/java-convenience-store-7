package store.view;

import camp.nextstep.edu.missionutils.Console;
import store.domain.Order;
import store.exception.ErrorMessage;
import store.exception.ServiceException;
import store.parser.DomainParser;
import store.parser.OrdersParser;

import java.util.List;

public class InputView {
    private static boolean isNo(String strippedInput) {
        return strippedInput.equalsIgnoreCase("no") || strippedInput.equalsIgnoreCase("n");
    }

    private static boolean isYes(String strippedInput) {
        return strippedInput.equalsIgnoreCase("yes") || strippedInput.equalsIgnoreCase("y");
    }

    public List<Order> readOrders() {
        String prompt = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
        DomainParser<List<Order>> ordersParser = new OrdersParser();
        return readWithRetry(prompt, ordersParser);
    }

    private <T> T readWithRetry(String prompt, DomainParser<T> parser) {
        while (true) {
            try {
                System.out.println(prompt);
                String input = Console.readLine();
                return parser.parse(input);
            } catch (ServiceException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private boolean checkYesOrNo(String input) {
        String strippedInput = input.strip();

        if (isYes(strippedInput)) {
            return true;
        }

        if (isNo(strippedInput)) {
            return false;
        }

        throw new ServiceException(ErrorMessage.INVALID_INPUT_FORMAT);
    }
}
