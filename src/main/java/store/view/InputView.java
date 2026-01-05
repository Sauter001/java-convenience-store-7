package store.view;

import camp.nextstep.edu.missionutils.Console;
import store.domain.io.BinaryResponse;
import store.error.StoreException;
import store.parser.io.Parser;

public class InputView {
    public BinaryResponse readKeepBuying() {
        String prompt = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)\n";
        return readWithRetry(prompt, BinaryResponse::getResponseFrom);
    }

    public BinaryResponse readOrders() {
        String prompt = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])\n";
        return readWithRetry(prompt, )
    }

    private <T> T readWithRetry(String prompt, Parser<T> parser) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = Console.readLine();
                return parser.parse(input);
            } catch (StoreException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
