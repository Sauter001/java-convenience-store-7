package store.domain.order;

import org.junit.jupiter.api.Test;
import store.domain.order.dto.OrderPresentedDto;
import store.domain.order.dto.OrderReceiptDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReceiptTest {

    @Test
    void should_calculate_final_amount_with_all_discounts() {
        // Given: 총액 20000, 프로모션 할인 3000, 멤버십 할인 2000
        Receipt.PaymentAmount paymentAmount = new Receipt.PaymentAmount(20000, 3000, 2000);

        // When: 최종 결제액 계산
        int finalAmount = paymentAmount.calculateFinalAmount();

        // Then: 20000 - 3000 - 2000 = 15000
        assertThat(finalAmount).isEqualTo(15000);
    }

    @Test
    void should_calculate_final_amount_with_only_promotion_discount() {
        // Given: 총액 10000, 프로모션 할인 2000, 멤버십 할인 0
        Receipt.PaymentAmount paymentAmount = new Receipt.PaymentAmount(10000, 2000, 0);

        // When: 최종 결제액 계산
        int finalAmount = paymentAmount.calculateFinalAmount();

        // Then: 10000 - 2000 = 8000
        assertThat(finalAmount).isEqualTo(8000);
    }

    @Test
    void should_calculate_final_amount_with_only_membership_discount() {
        // Given: 총액 10000, 프로모션 할인 0, 멤버십 할인 1500
        Receipt.PaymentAmount paymentAmount = new Receipt.PaymentAmount(10000, 0, 1500);

        // When: 최종 결제액 계산
        int finalAmount = paymentAmount.calculateFinalAmount();

        // Then: 10000 - 1500 = 8500
        assertThat(finalAmount).isEqualTo(8500);
    }

    @Test
    void should_return_total_when_no_discounts() {
        // Given: 총액 10000, 할인 없음
        Receipt.PaymentAmount paymentAmount = new Receipt.PaymentAmount(10000, 0, 0);

        // When: 최종 결제액 계산
        int finalAmount = paymentAmount.calculateFinalAmount();

        // Then: 10000
        assertThat(finalAmount).isEqualTo(10000);
    }

    @Test
    void should_calculate_total_quantity_from_purchased_items() {
        // Given: Receipt with purchasedItems
        List<OrderReceiptDto> purchasedItems = List.of(
                new OrderReceiptDto("콜라", 5, 5000),
                new OrderReceiptDto("사이다", 3, 3000)
        );
        Receipt receipt = createReceipt(purchasedItems, List.of(), 8000, 0, 0);

        // When: 총 구매 수량 계산
        int totalQuantity = receipt.totalQuantity();

        // Then: 5 + 3 = 8개
        assertThat(totalQuantity).isEqualTo(8);
    }

    @Test
    void should_return_zero_quantity_when_empty_receipt() {
        // Given: 빈 purchasedItems
        Receipt receipt = createReceipt(List.of(), List.of(), 0, 0, 0);

        // When: 총 구매 수량 계산
        int totalQuantity = receipt.totalQuantity();

        // Then: 0
        assertThat(totalQuantity).isEqualTo(0);
    }

    @Test
    void should_create_receipt_with_empty_presented_items() {
        // Given: presentedItems가 빈 리스트
        List<OrderReceiptDto> purchasedItems = List.of(
                new OrderReceiptDto("콜라", 5, 5000)
        );
        List<OrderPresentedDto> presentedItems = List.of();

        // When: Receipt 생성
        Receipt receipt = createReceipt(purchasedItems, presentedItems, 5000, 0, 0);

        // Then: presentedItems가 빈 리스트
        assertThat(receipt.presentedItems()).isEmpty();
        assertThat(receipt.purchasedItems()).hasSize(1);
    }

    // Fixture 헬퍼 메서드
    private Receipt createReceipt(List<OrderReceiptDto> purchased,
                                  List<OrderPresentedDto> presented,
                                  int total, int promoDiscount, int memberDiscount) {
        Receipt.PaymentAmount payment = new Receipt.PaymentAmount(total, promoDiscount, memberDiscount);
        return new Receipt(purchased, presented, payment);
    }
}
