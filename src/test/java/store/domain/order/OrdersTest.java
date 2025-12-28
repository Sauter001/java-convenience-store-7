package store.domain.order;

import org.junit.jupiter.api.Test;
import store.domain.order.dto.OrderPresentedDto;
import store.domain.order.dto.OrderReceiptDto;
import store.domain.product.Product;
import store.domain.product.Stock;
import store.domain.promotion.BuyGetQuantity;
import store.domain.promotion.Promotion;
import store.domain.promotion.PromotionPeriod;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrdersTest {

    @Test
    void should_calculate_30_percent_membership_discount() {
        // Given: 할인 대상 금액 10000원 (프로모션 없는 상품)
        Order order = createOrderWithoutPromotion("콜라", 1000, 10, 10);
        Orders orders = new Orders(List.of(order));

        // When: 멤버십 할인 계산
        int discount = orders.calculateMembershipDiscount();

        // Then: 10000 * 0.3 = 3000원
        assertThat(discount).isEqualTo(3000);
    }

    @Test
    void should_limit_membership_discount_to_8000() {
        // Given:
        //   - Order1: 프로모션 없음, 20000원
        //   - Order2: 프로모션 없음, 10000원
        //   총 할인 대상: 30000원 → 할인액 9000원이지만
        Order order1 = createOrderWithoutPromotion("정식도시락", 6400, 5, 20);
        Order order2 = createOrderWithoutPromotion("물", 500, 10, 20);
        Orders orders = new Orders(List.of(order1, order2));

        // When: 멤버십 할인 계산
        int discount = orders.calculateMembershipDiscount();

        // Then: 8000원 한도
        assertThat(discount).isEqualTo(8000);
    }

    @Test
    void should_return_zero_when_all_orders_have_promotion() {
        // Given: 모든 주문이 프로모션 완전 적용 (할인 대상 0원)
        Order order1 = createOrderWithPromotion("콜라", 1000, 10, 10, create2Plus1Promotion(), 6);
        Order order2 = createOrderWithPromotion("사이다", 1000, 10, 10, create2Plus1Promotion(), 6);
        Orders orders = new Orders(List.of(order1, order2));

        // When: 멤버십 할인 계산
        int discount = orders.calculateMembershipDiscount();

        // Then: 0원
        assertThat(discount).isEqualTo(0);
    }

    @Test
    void should_calculate_mixed_promotion_and_regular() {
        // Given:
        //   - Order1: 2+1 부분 적용, 정가 부분 5000원
        //   - Order2: 프로모션 없음, 3000원
        //   총 할인 대상: 8000원
        Order order1 = createOrderWithPromotion("콜라", 1000, 10, 6, create2Plus1Promotion(), 11);
        Order order2 = createOrderWithoutPromotion("물", 500, 6, 10);
        Orders orders = new Orders(List.of(order1, order2));

        // When: 멤버십 할인 계산
        int discount = orders.calculateMembershipDiscount();

        // Then: 8000 * 0.3 = 2400원
        assertThat(discount).isEqualTo(2400);
    }

    @Test
    void should_round_membership_discount_correctly() {
        // Given: 할인 대상 10001원
        //        10001 * 0.3 = 3000.3 → Math.round → 3000
        Order order = createOrderWithoutPromotion("상품", 10001, 1, 10);
        Orders orders = new Orders(List.of(order));

        // When: 멤버십 할인 계산
        int discount = orders.calculateMembershipDiscount();

        // Then: 3000원 (반올림)
        assertThat(discount).isEqualTo(3000);
    }

    @Test
    void should_sum_all_promotion_discounts() {
        // Given:
        //   - Order1: 프로모션 할인 2000원
        //   - Order2: 프로모션 할인 3000원
        //   - Order3: 프로모션 없음 (0원)
        Order order1 = createOrderWithPromotion("콜라", 1000, 10, 10, create2Plus1Promotion(), 6);
        Order order2 = createOrderWithPromotion("오렌지주스", 1500, 10, 10, create2Plus1Promotion(), 6);
        Order order3 = createOrderWithoutPromotion("물", 500, 5, 10);
        Orders orders = new Orders(List.of(order1, order2, order3));

        // When: 프로모션 할인 합계
        int totalDiscount = orders.getPromotionDiscount();

        // Then: 2000 + 3000 = 5000원
        assertThat(totalDiscount).isEqualTo(5000);
    }

    @Test
    void should_return_zero_when_no_promotion_discounts() {
        // Given: 모든 주문이 프로모션 없음
        Order order1 = createOrderWithoutPromotion("콜라", 1000, 5, 10);
        Order order2 = createOrderWithoutPromotion("물", 500, 10, 20);
        Orders orders = new Orders(List.of(order1, order2));

        // When: 프로모션 할인 합계
        int totalDiscount = orders.getPromotionDiscount();

        // Then: 0원
        assertThat(totalDiscount).isEqualTo(0);
    }

    @Test
    void should_calculate_total_full_amount() {
        // Given:
        //   - Order1: 5000원
        //   - Order2: 3000원
        //   - Order3: 10000원
        Order order1 = createOrderWithoutPromotion("콜라", 1000, 5, 10);
        Order order2 = createOrderWithoutPromotion("사이다", 1000, 3, 10);
        Order order3 = createOrderWithoutPromotion("물", 500, 20, 30);
        Orders orders = new Orders(List.of(order1, order2, order3));

        // When: 총 구매액 계산
        int totalAmount = orders.getFullAmount();

        // Then: 18000원
        assertThat(totalAmount).isEqualTo(18000);
    }

    @Test
    void should_convert_all_orders_to_receipt_dtos() {
        // Given: 3개의 주문
        Order order1 = createOrderWithoutPromotion("콜라", 1000, 5, 10);
        Order order2 = createOrderWithoutPromotion("사이다", 1000, 3, 10);
        Order order3 = createOrderWithoutPromotion("물", 500, 10, 30);
        Orders orders = new Orders(List.of(order1, order2, order3));

        // When: ReceiptDto 리스트로 변환
        List<OrderReceiptDto> dtos = orders.toReceiptDtos();

        // Then: 3개 DTO, 내용 검증
        assertThat(dtos).hasSize(3);
        assertThat(dtos.get(0).productName()).isEqualTo("콜라");
        assertThat(dtos.get(0).quantity()).isEqualTo(5);
        assertThat(dtos.get(0).cost()).isEqualTo(5000);
    }

    @Test
    void should_convert_to_presented_dtos_filtering_nulls() {
        // Given:
        //   - Order1: 프로모션 있음 (증정 DTO 생성)
        //   - Order2: 프로모션 없음 (null)
        //   - Order3: 프로모션 있음 (증정 DTO 생성)
        Order order1 = createOrderWithPromotion("콜라", 1000, 10, 10, create2Plus1Promotion(), 6);
        Order order2 = createOrderWithoutPromotion("물", 500, 5, 10);
        Order order3 = createOrderWithPromotion("사이다", 1000, 10, 10, create2Plus1Promotion(), 6);
        Orders orders = new Orders(List.of(order1, order2, order3));

        // When: PresentedDto 리스트로 변환
        List<OrderPresentedDto> dtos = orders.toPresentedDtos();

        // Then: 2개 DTO (null 필터링됨)
        assertThat(dtos).hasSize(2);
        assertThat(dtos.get(0).productName()).isEqualTo("콜라");
        assertThat(dtos.get(1).productName()).isEqualTo("사이다");
    }

    @Test
    void should_return_empty_list_when_no_presented_items() {
        // Given: 모든 주문이 프로모션 없음
        Order order1 = createOrderWithoutPromotion("콜라", 1000, 5, 10);
        Order order2 = createOrderWithoutPromotion("물", 500, 5, 10);
        Orders orders = new Orders(List.of(order1, order2));

        // When: PresentedDto 리스트로 변환
        List<OrderPresentedDto> dtos = orders.toPresentedDtos();

        // Then: 빈 리스트
        assertThat(dtos).isEmpty();
    }

    // Fixture 헬퍼 메서드
    private Order createOrderWithPromotion(String name, int price, int normalStock,
                                           int promotionStock, Promotion promotion, int quantity) {
        Stock stock = new Stock(normalStock, promotionStock);
        Product product = new Product(name, price, stock, promotion);
        return new Order(product, quantity);
    }

    private Order createOrderWithoutPromotion(String name, int price, int quantity, int stock) {
        Product product = new Product(name, price, new Stock(stock, 0), null);
        return new Order(product, quantity);
    }

    private Promotion create2Plus1Promotion() {
        return createActivePromotion("탄산2+1", 2, 1);
    }

    private Promotion createActivePromotion(String name, int buy, int get) {
        BuyGetQuantity buyGetQuantity = new BuyGetQuantity(buy, get);
        PromotionPeriod period = new PromotionPeriod(
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(30)
        );
        return new Promotion(name, buyGetQuantity, period);
    }
}
