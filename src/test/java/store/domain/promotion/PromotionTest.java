package store.domain.promotion;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class PromotionTest {

    @Test
    void should_suggest_when_remainder_equals_required_and_stock_sufficient() {
        // Given: 2+1 프로모션, 주문 수량 2개 (remainder=2, required=2), 재고 충분 (10개)
        Promotion promotion = create2Plus1Promotion();

        // When: 추가 제안 여부 확인
        boolean shouldSuggest = promotion.shouldSuggestAdditional(2, 10);

        // Then: true (2개 주문 시 1개 더 받을 수 있음)
        assertThat(shouldSuggest).isTrue();
    }

    @Test
    void should_not_suggest_when_remainder_not_equals_required() {
        // Given: 2+1 프로모션, 주문 수량 3개 (remainder=0, required=2)
        Promotion promotion = create2Plus1Promotion();

        // When: 추가 제안 여부 확인
        boolean shouldSuggest = promotion.shouldSuggestAdditional(3, 10);

        // Then: false (이미 세트 완성)
        assertThat(shouldSuggest).isFalse();
    }

    @Test
    void should_not_suggest_when_stock_insufficient_for_additional() {
        // Given: 2+1 프로모션, 주문 수량 2개, 재고 2개 (3개 필요하지만 2개만 있음)
        Promotion promotion = create2Plus1Promotion();

        // When: 추가 제안 여부 확인
        boolean shouldSuggest = promotion.shouldSuggestAdditional(2, 2);

        // Then: false (재고 부족)
        assertThat(shouldSuggest).isFalse();
    }

    @Test
    void should_suggest_for_1_plus_1_promotion() {
        // Given: 1+1 프로모션, 주문 수량 1개 (remainder=1, required=1), 재고 충분
        Promotion promotion = create1Plus1Promotion();

        // When: 추가 제안 여부 확인
        boolean shouldSuggest = promotion.shouldSuggestAdditional(1, 5);

        // Then: true (1개 주문 시 1개 더 받을 수 있음)
        assertThat(shouldSuggest).isTrue();
    }

    @Test
    void should_suggest_for_multiple_sets_in_2_plus_1() {
        // Given: 2+1 프로모션, 주문 수량 5개 (1세트 완료 + 2개), 재고 충분
        //        5 % 3 = 2 (remainder), required = 2
        Promotion promotion = create2Plus1Promotion();

        // When: 추가 제안 여부 확인
        boolean shouldSuggest = promotion.shouldSuggestAdditional(5, 10);

        // Then: true (5개 주문하면 1개 더 받을 수 있음)
        assertThat(shouldSuggest).isTrue();
    }

    @Test
    void should_return_correct_set_size() {
        // Given: 2+1 프로모션
        Promotion promotion = create2Plus1Promotion();

        // When: 세트 크기 확인
        int setSize = promotion.getSetSize();

        // Then: 3
        assertThat(setSize).isEqualTo(3);
    }

    @Test
    void should_return_correct_required_quantity() {
        // Given: 2+1 프로모션
        Promotion promotion = create2Plus1Promotion();

        // When: 필수 수량 확인
        int requiredQuantity = promotion.getRequiredQuantity();

        // Then: 2
        assertThat(requiredQuantity).isEqualTo(2);
    }

    @Test
    void should_return_correct_bonus_quantity() {
        // Given: 2+1 프로모션
        Promotion promotion = create2Plus1Promotion();

        // When: 증정 수량 확인
        int bonusQuantity = promotion.getBonusQuantity();

        // Then: 1
        assertThat(bonusQuantity).isEqualTo(1);
    }

    // Fixture 헬퍼 메서드
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
