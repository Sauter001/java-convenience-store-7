package store.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.order.dto.PromotionConfirmation;
import store.domain.promotion.BuyGetQuantity;
import store.domain.promotion.Promotion;
import store.domain.promotion.PromotionPeriod;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class PromotionSuggestionTest {

    private Promotion createActivePromotion(String name, int buy, int get) {
        BuyGetQuantity buyGetQuantity = new BuyGetQuantity(buy, get);
        PromotionPeriod period = new PromotionPeriod(
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2026, 1, 31)
        );
        return new Promotion(name, buyGetQuantity, period);
    }

    @Test
    @DisplayName("2+1 프로모션에서 콜라 2개 주문 시 추가 증정 제안해야 함")
    void should_suggest_additional_item_when_order_2_in_2plus1() {
        // Given: 2+1 프로모션, 프로모션 재고 10개
        Stock stock = new Stock(10, 10);
        Promotion promotion = createActivePromotion("탄산2+1", 2, 1);
        Product product = new Product("콜라", 1000, stock, promotion);

        // When
        boolean shouldSuggest = product.shouldSuggestAdditionalItem(2);

        // Then
        assertThat(shouldSuggest).isTrue();
        assertThat(product.getAdditionalQuantity(2)).isEqualTo(1);
    }

    @Test
    @DisplayName("2+1 프로모션에서 콜라 5개 주문 시 추가 증정 제안해야 함")
    void should_suggest_additional_item_when_order_5_in_2plus1() {
        // Given: 2+1 프로모션, 프로모션 재고 10개
        Stock stock = new Stock(10, 10);
        Promotion promotion = createActivePromotion("탄산2+1", 2, 1);
        Product product = new Product("콜라", 1000, stock, promotion);

        // When
        boolean shouldSuggest = product.shouldSuggestAdditionalItem(5);

        // Then
        assertThat(shouldSuggest).isTrue();
        assertThat(product.getAdditionalQuantity(5)).isEqualTo(1);
    }

    @Test
    @DisplayName("2+1 프로모션에서 콜라 3개 주문 시 추가 증정 제안 안 해야 함")
    void should_not_suggest_when_order_3_in_2plus1() {
        // Given: 2+1 프로모션, 프로모션 재고 10개
        Stock stock = new Stock(10, 10);
        Promotion promotion = createActivePromotion("탄산2+1", 2, 1);
        Product product = new Product("콜라", 1000, stock, promotion);

        // When
        boolean shouldSuggest = product.shouldSuggestAdditionalItem(3);

        // Then
        assertThat(shouldSuggest).isFalse();
    }

    @Test
    @DisplayName("2+1 프로모션에서 콜라 4개 주문 시 추가 증정 제안 안 해야 함")
    void should_not_suggest_when_order_4_in_2plus1() {
        // Given: 2+1 프로모션, 프로모션 재고 10개
        Stock stock = new Stock(10, 10);
        Promotion promotion = createActivePromotion("탄산2+1", 2, 1);
        Product product = new Product("콜라", 1000, stock, promotion);

        // When
        boolean shouldSuggest = product.shouldSuggestAdditionalItem(4);

        // Then
        assertThat(shouldSuggest).isFalse();
    }

    @Test
    @DisplayName("프로모션 재고 부족 시 추가 증정 제안 안 해야 함")
    void should_not_suggest_when_promotion_stock_insufficient() {
        // Given: 2+1 프로모션, 프로모션 재고 2개만
        Stock stock = new Stock(10, 2);
        Promotion promotion = createActivePromotion("탄산2+1", 2, 1);
        Product product = new Product("콜라", 1000, stock, promotion);

        // When: 2개 주문 (보통은 제안하지만 재고 부족)
        boolean shouldSuggest = product.shouldSuggestAdditionalItem(2);

        // Then
        assertThat(shouldSuggest).isFalse();
    }

    @Test
    @DisplayName("콜라 15개 주문 시 부분 적용 안내해야 함")
    void should_return_partially_applicable_when_order_15() {
        // Given: 2+1 프로모션, 프로모션 재고 10개, 일반 재고 10개
        Stock stock = new Stock(10, 10);
        Promotion promotion = createActivePromotion("탄산2+1", 2, 1);
        Product product = new Product("콜라", 1000, stock, promotion);

        // When
        PromotionConfirmation confirmation = product.getPromotionConfirmation(15);

        // Then
        assertThat(confirmation).isInstanceOf(PromotionConfirmation.PartiallyApplicable.class);
        PromotionConfirmation.PartiallyApplicable partial =
            (PromotionConfirmation.PartiallyApplicable) confirmation;
        assertThat(partial.promotionQuantity()).isEqualTo(10);
        assertThat(partial.regularPriceQuantity()).isEqualTo(5);
    }

    @Test
    @DisplayName("콜라 10개 주문 시 완전 적용해야 함")
    void should_return_fully_applicable_when_order_10() {
        // Given: 2+1 프로모션, 프로모션 재고 10개 이상
        Stock stock = new Stock(10, 10);
        Promotion promotion = createActivePromotion("탄산2+1", 2, 1);
        Product product = new Product("콜라", 1000, stock, promotion);

        // When
        PromotionConfirmation confirmation = product.getPromotionConfirmation(10);

        // Then
        assertThat(confirmation).isInstanceOf(PromotionConfirmation.FullyApplicable.class);
        PromotionConfirmation.FullyApplicable fully =
            (PromotionConfirmation.FullyApplicable) confirmation;
        assertThat(fully.totalQuantity()).isEqualTo(10);
    }

    @Test
    @DisplayName("1+1 프로모션에서 오렌지주스 1개 주문 시 추가 증정 제안해야 함")
    void should_suggest_additional_item_in_1plus1() {
        // Given: 1+1 프로모션, 프로모션 재고 9개
        Stock stock = new Stock(0, 9);
        Promotion promotion = createActivePromotion("MD추천상품", 1, 1);
        Product product = new Product("오렌지주스", 1800, stock, promotion);

        // When
        boolean shouldSuggest = product.shouldSuggestAdditionalItem(1);

        // Then
        assertThat(shouldSuggest).isTrue();
        assertThat(product.getAdditionalQuantity(1)).isEqualTo(1);
    }
}
