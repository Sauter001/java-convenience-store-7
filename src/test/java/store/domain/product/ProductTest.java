package store.domain.product;

import org.junit.jupiter.api.Test;
import store.domain.order.dto.PromotionConfirmation;
import store.domain.promotion.BuyGetQuantity;
import store.domain.promotion.Promotion;
import store.domain.promotion.PromotionPeriod;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    @Test
    void should_return_fully_applicable_when_promotion_stock_sufficient() {
        // Given: 프로모션 재고 10개, 주문 5개
        Stock stock = new Stock(5, 10);
        Promotion promotion = createActivePromotion("탄산2+1", 2, 1);
        Product product = new Product("콜라", 1000, stock, promotion);

        // When
        PromotionConfirmation result = product.getPromotionConfirmation(5);

        // Then
        assertThat(result).isInstanceOf(PromotionConfirmation.FullyApplicable.class);
        PromotionConfirmation.FullyApplicable fully = (PromotionConfirmation.FullyApplicable) result;
        assertThat(fully.productName()).isEqualTo("콜라");
        assertThat(fully.totalQuantity()).isEqualTo(5);
    }

    @Test
    void should_return_partially_applicable_when_promotion_stock_insufficient() {
        // Given: 프로모션 재고 5개, 일반 재고 10개, 주문 7개
        Stock stock = new Stock(10, 5);
        Promotion promotion = createActivePromotion("탄산2+1", 2, 1);
        Product product = new Product("콜라", 1000, stock, promotion);

        // When
        PromotionConfirmation result = product.getPromotionConfirmation(7);

        // Then
        assertThat(result).isInstanceOf(PromotionConfirmation.PartiallyApplicable.class);
        PromotionConfirmation.PartiallyApplicable partial = (PromotionConfirmation.PartiallyApplicable) result;
        assertThat(partial.productName()).isEqualTo("콜라");
        assertThat(partial.promotionQuantity()).isEqualTo(5);
        assertThat(partial.regularPriceQuantity()).isEqualTo(2);
    }

    @Test
    void should_throw_exception_when_total_stock_insufficient() {
        // Given: 프로모션 재고 3개, 일반 재고 5개, 주문 10개
        Stock stock = new Stock(5, 3);
        Promotion promotion = createActivePromotion("탄산2+1", 2, 1);
        Product product = new Product("콜라", 1000, stock, promotion);

        // When & Then
        assertThatThrownBy(() -> product.getPromotionConfirmation(10))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void should_return_stock_state_promotion_only() {
        // Given
        Stock stock = new Stock(10, 10);
        Product product = new Product("콜라", 1000, stock);

        // When
        StockState state = product.getStockState(5);

        // Then
        assertThat(state).isEqualTo(StockState.PROMOTION_ONLY);
    }

    @Test
    void should_return_stock_state_promotion_with_normal() {
        // Given
        Stock stock = new Stock(10, 5);
        Product product = new Product("콜라", 1000, stock);

        // When
        StockState state = product.getStockState(7);

        // Then
        assertThat(state).isEqualTo(StockState.PROMOTION_WITH_NORMAL);
    }

    @Test
    void should_return_stock_state_insufficient() {
        // Given
        Stock stock = new Stock(5, 3);
        Product product = new Product("콜라", 1000, stock);

        // When
        StockState state = product.getStockState(10);

        // Then
        assertThat(state).isEqualTo(StockState.INSUFFICIENT);
    }

    @Test
    void should_have_promotion_when_promotion_active() {
        // Given
        Stock stock = new Stock(10, 10);
        Promotion promotion = createActivePromotion("탄산2+1", 2, 1);
        Product product = new Product("콜라", 1000, stock, promotion);

        // When & Then
        assertThat(product.hasPromotion()).isTrue();
    }

    @Test
    void should_not_have_promotion_when_promotion_null() {
        // Given
        Stock stock = new Stock(10, 10);
        Product product = new Product("콜라", 1000, stock, null);

        // When & Then
        assertThat(product.hasPromotion()).isFalse();
    }

    @Test
    void should_not_have_promotion_when_promotion_expired() {
        // Given
        Stock stock = new Stock(10, 10);
        Promotion expiredPromotion = createExpiredPromotion("탄산2+1", 2, 1);
        Product product = new Product("콜라", 1000, stock, expiredPromotion);

        // When & Then
        assertThat(product.hasPromotion()).isFalse();
    }

    private Promotion createActivePromotion(String name, int buy, int get) {
        BuyGetQuantity buyGetQuantity = new BuyGetQuantity(buy, get);
        PromotionPeriod period = new PromotionPeriod(
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(30)
        );
        return new Promotion(name, buyGetQuantity, period);
    }

    private Promotion createExpiredPromotion(String name, int buy, int get) {
        BuyGetQuantity buyGetQuantity = new BuyGetQuantity(buy, get);
        PromotionPeriod period = new PromotionPeriod(
                LocalDate.now().minusDays(30),
                LocalDate.now().minusDays(1)
        );
        return new Promotion(name, buyGetQuantity, period);
    }
}
