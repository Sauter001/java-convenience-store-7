package store.view;

import camp.nextstep.edu.missionutils.Console;
import store.domain.io.BinaryResponse;
import store.domain.order.dto.OrderForm;
import store.domain.order.dto.PromotionConfirmation;
import store.exception.ServiceException;
import store.parser.input.OrderFormParser;
import store.parser.input.Parser;

import java.util.List;

public class InputView {
    private static final String YES_OR_NO = " (Y/N)";

    public List<OrderForm> readOrders() {
        String prompt = "구매하실 상품명과 수량을 입력해 주세요. (예 [사이다-2],[감자칩-1])\n";
        return readWithRetry(prompt, new OrderFormParser());
    }

    public BinaryResponse confirmPartialPromotion(PromotionConfirmation.PartiallyApplicable partial) {
        String message = String.format(
                "현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까?" + YES_OR_NO,
                partial.productName(),
                partial.regularPriceQuantity()
        );
        return confirm(message);
    }

    public BinaryResponse confirmAdditionalItem(String productName, int additionalQuantity) {
        String message = String.format(
                "현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까?" + YES_OR_NO + "\n",
                productName,
                additionalQuantity
        );
        return confirm(message);
    }

    private BinaryResponse confirm(String prompt) {
        return readWithRetry(prompt, BinaryResponse::from);
    }

    private <T> T readWithRetry(String prompt, Parser<T> parser) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = Console.readLine();
                System.out.println();
                return parser.parse(input);
            } catch (ServiceException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public BinaryResponse confirmMembership() {
        String message = "멤버십 할인을 받으시겠습니까?" + YES_OR_NO + "\n";
        return confirm(message);
    }
}
