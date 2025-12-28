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

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {

    @Test
    void should_return_full_amount_as_discountable_when_no_promotion() {
        // Given: 프로모션 없는 상품, 가격 1000원, 수량 5개
        Product product = createProductWithoutPromotion("콜라", 1000, 10);
        Order order = new Order(product, 5);

        // When: 멤버십 할인 대상 금액 확인
        int discountableAmount = order.getDiscountableAmount();

        // Then: 5000원 전체가 멤버십 할인 대상
        assertThat(discountableAmount).isEqualTo(5000);
    }

    @Test
    void should_return_zero_discountable_when_fully_applicable() {
        // Given: 2+1 프로모션 완전 적용, 가격 1000원, 수량 6개
        Product product = createProductWithPromotion("콜라", 1000, 10, 10, create2Plus1Promotion());
        Order order = new Order(product, 6);

        // When: 멤버십 할인 대상 금액 확인
        int discountableAmount = order.getDiscountableAmount();

        // Then: 0원 (프로모션 적용되면 멤버십 할인 불가)
        assertThat(discountableAmount).isEqualTo(0);
    }

    @Test
    void should_return_regular_price_portion_when_partially_applicable() {
        // Given: 2+1, 프로모션재고=6, 일반재고=10, 주문=10개, 가격 1000원
        //        maxSets=6/3=2, promotionQuantity=6, regularPriceQuantity=4
        Product product = createProductWithPromotion("콜라", 1000, 10, 6, create2Plus1Promotion());
        Order order = new Order(product, 10);

        // When: 멤버십 할인 대상 금액 확인
        int discountableAmount = order.getDiscountableAmount();

        // Then: 4000원 (정가 구매 부분만)
        assertThat(discountableAmount).isEqualTo(4000);
    }

    @Test
    void should_return_discount_when_fully_applicable() {
        // Given: 2+1, 가격 1000원, 수량 6개 (완전 적용)
        //        6 / 3 = 2세트 → 2개 무료
        Product product = createProductWithPromotion("콜라", 1000, 10, 10, create2Plus1Promotion());
        Order order = new Order(product, 6);

        // When: 프로모션 할인액 확인
        int discount = order.getPromotionDiscount();

        // Then: 2000원
        assertThat(discount).isEqualTo(2000);
    }

    @Test
    void should_return_discount_when_partially_applicable() {
        // Given: 2+1, 프로모션재고=6, 일반재고=10, 주문=10개, 가격 1000원
        //        promotionQuantity=6, 6 / 3 = 2세트 → 2개 무료
        Product product = createProductWithPromotion("콜라", 1000, 10, 6, create2Plus1Promotion());
        Order order = new Order(product, 10);

        // When: 프로모션 할인액 확인
        int discount = order.getPromotionDiscount();

        // Then: 2000원
        assertThat(discount).isEqualTo(2000);
    }

    @Test
    void should_return_zero_discount_when_no_promotion() {
        // Given: 프로모션 없음, 수량 5개
        Product product = createProductWithoutPromotion("콜라", 1000, 10);
        Order order = new Order(product, 5);

        // When: 프로모션 할인액 확인
        int discount = order.getPromotionDiscount();

        // Then: 0원
        assertThat(discount).isEqualTo(0);
    }

    @Test
    void should_calculate_discount_for_1_plus_1() {
        // Given: 1+1, 가격 1800원, 수량 4개 (완전 적용)
        //        4 / 2 = 2세트 → 2개 무료
        Product product = createProductWithPromotion("오렌지주스", 1800, 10, 10, create1Plus1Promotion());
        Order order = new Order(product, 4);

        // When: 프로모션 할인액 확인
        int discount = order.getPromotionDiscount();

        // Then: 3600원
        assertThat(discount).isEqualTo(3600);
    }

    @Test
    void should_return_presented_quantity_for_fully_applicable() {
        // Given: 2+1, 수량 9개 (완전 적용)
        //        9 / 3 = 3세트 → 3개 증정
        Product product = createProductWithPromotion("콜라", 1000, 10, 10, create2Plus1Promotion());
        Order order = new Order(product, 9);

        // When: 증정 수량 확인
        int presentedQuantity = order.getPresentedQuantity();

        // Then: 3개
        assertThat(presentedQuantity).isEqualTo(3);
    }

    @Test
    void should_return_presented_quantity_for_partially_applicable() {
        // Given: 2+1, promotionQuantity=6 (부분 적용)
        //        6 / 3 = 2세트 → 2개 증정
        Product product = createProductWithPromotion("콜라", 1000, 10, 6, create2Plus1Promotion());
        Order order = new Order(product, 10);

        // When: 증정 수량 확인
        int presentedQuantity = order.getPresentedQuantity();

        // Then: 2개
        assertThat(presentedQuantity).isEqualTo(2);
    }

    @Test
    void should_return_zero_presented_when_no_promotion() {
        // Given: 프로모션 없음
        Product product = createProductWithoutPromotion("콜라", 1000, 10);
        Order order = new Order(product, 5);

        // When: 증정 수량 확인
        int presentedQuantity = order.getPresentedQuantity();

        // Then: 0개
        assertThat(presentedQuantity).isEqualTo(0);
    }

    @Test
    void should_adjust_quantity() {
        // Given: 수량 5개인 주문
        Product product = createProductWithoutPromotion("콜라", 1000, 10);
        Order order = new Order(product, 5);

        // When: 수량을 3개로 조정
        order.adjustQuantity(3);

        // Then: 수량이 3개로 변경
        assertThat(order.getQuantity()).isEqualTo(3);
    }

    @Test
    void should_increase_quantity() {
        // Given: 수량 5개인 주문
        Product product = createProductWithoutPromotion("콜라", 1000, 10);
        Order order = new Order(product, 5);

        // When: 수량 2개 증가
        order.increaseQuantity(2);

        // Then: 수량이 7개로 변경
        assertThat(order.getQuantity()).isEqualTo(7);
    }

    @Test
    void should_convert_to_receipt_dto() {
        // Given: 상품명="콜라", 수량=5, 가격=1000
        Product product = createProductWithoutPromotion("콜라", 1000, 10);
        Order order = new Order(product, 5);

        // When: ReceiptDto로 변환
        OrderReceiptDto dto = order.toReceiptDto();

        // Then: 필드값 검증
        assertThat(dto.productName()).isEqualTo("콜라");
        assertThat(dto.quantity()).isEqualTo(5);
        assertThat(dto.cost()).isEqualTo(5000);
    }

    @Test
    void should_return_null_when_no_presented_items() {
        // Given: 프로모션 없는 주문
        Product product = createProductWithoutPromotion("콜라", 1000, 10);
        Order order = new Order(product, 5);

        // When: PresentedDto로 변환
        OrderPresentedDto dto = order.toPresentedDto();

        // Then: null 반환
        assertThat(dto).isNull();
    }

    @Test
    void should_convert_to_presented_dto_when_has_promotion() {
        // Given: 2+1, 수량=6개 (2개 증정)
        Product product = createProductWithPromotion("콜라", 1000, 10, 10, create2Plus1Promotion());
        Order order = new Order(product, 6);

        // When: PresentedDto로 변환
        OrderPresentedDto dto = order.toPresentedDto();

        // Then: 필드값 검증
        assertThat(dto).isNotNull();
        assertThat(dto.productName()).isEqualTo("콜라");
        assertThat(dto.quantity()).isEqualTo(2);
    }

    // Fixture 헬퍼 메서드
    private Product createProductWithPromotion(String name, int price, int normalStock,
                                               int promotionStock, Promotion promotion) {
        Stock stock = new Stock(normalStock, promotionStock);
        return new Product(name, price, stock, promotion);
    }

    private Product createProductWithoutPromotion(String name, int price, int stock) {
        return new Product(name, price, new Stock(stock, 0), null);
    }

    private Promotion create2Plus1Promotion() {
        return createActivePromotion("탄산2+1", 2, 1);
    }

    private Promotion create1Plus1Promotion() {
        return createActivePromotion("MD추천상품", 1, 1);
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
