package store.view;

import camp.nextstep.edu.missionutils.Console;
import store.domain.io.BinaryResponse;
import store.domain.order.Order;
import store.domain.order.dto.OrderForm;
import store.exception.ServiceException;
import store.parser.input.OrderFormParser;
import store.parser.input.Parser;

import java.util.List;

public class InputView {
    public List<OrderForm> readOrders() {
        String prompt = "구매하실 상품명과 수량을 입력해 주세요. (예 [사이다-2],[감자칩-1])\n";
        return readWithRetry(prompt, new OrderFormParser());
    }

    public BinaryResponse confirmOrder(Order order) {

    }

    private BinaryResponse confirm(String prompt) {
        System.out.println(prompt);
        String input = Console.readLine().strip();
        return BinaryResponse.from(input);
    }

    private <T> T readWithRetry(String prompt, Parser<T> parser) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = Console.readLine();
                System.out.println();
                return parser.parse(input);
            }  catch (ServiceException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
