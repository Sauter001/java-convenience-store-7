package store.domain.product;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StockTest {

    @Test
    void should_return_promotion_only_when_promotion_stock_sufficient() {
        // Given: 프로모션 재고 10개, 일반 재고 5개, 주문 수량 8개
        Stock stock = createStock(5, 10);

        // When: 재고 상태 확인
        StockState state = stock.checkStockState(8);

        // Then: 프로모션 재고만으로 충분
        assertThat(state).isEqualTo(StockState.PROMOTION_ONLY);
    }

    @Test
    void should_return_promotion_with_normal_when_promotion_stock_insufficient() {
        // Given: 프로모션 재고 3개, 일반 재고 10개, 주문 수량 8개
        Stock stock = createStock(10, 3);

        // When: 재고 상태 확인
        StockState state = stock.checkStockState(8);

        // Then: 프로모션 + 일반 재고 모두 필요
        assertThat(state).isEqualTo(StockState.PROMOTION_WITH_NORMAL);
    }

    @Test
    void should_return_insufficient_when_total_stock_not_enough() {
        // Given: 프로모션 재고 3개, 일반 재고 5개, 주문 수량 10개
        Stock stock = createStock(5, 3);

        // When: 재고 상태 확인
        StockState state = stock.checkStockState(10);

        // Then: 전체 재고 부족
        assertThat(state).isEqualTo(StockState.INSUFFICIENT);
    }

    @Test
    void should_return_promotion_only_when_quantity_equals_promotion_stock() {
        // Given: 프로모션 재고 10개, 일반 재고 5개, 주문 수량 10개 (경계값)
        Stock stock = createStock(5, 10);

        // When: 재고 상태 확인
        StockState state = stock.checkStockState(10);

        // Then: 프로모션 재고와 정확히 일치
        assertThat(state).isEqualTo(StockState.PROMOTION_ONLY);
    }

    @Test
    void should_return_promotion_with_normal_when_quantity_equals_total_stock() {
        // Given: 프로모션 재고 5개, 일반 재고 5개, 주문 수량 10개 (경계값)
        Stock stock = createStock(5, 5);

        // When: 재고 상태 확인
        StockState state = stock.checkStockState(10);

        // Then: 전체 재고와 정확히 일치
        assertThat(state).isEqualTo(StockState.PROMOTION_WITH_NORMAL);
    }

    @Test
    void should_decrease_promotion_stock_only() {
        // Given: 일반 재고 10개, 프로모션 재고 10개
        Stock stock = createStock(10, 10);

        // When: 프로모션 재고 5개만 차감
        stock.decrease(5, 0);

        // Then: 프로모션 재고만 감소
        assertThat(stock.getPromotionStock()).isEqualTo(5);
        assertThat(stock.getNormalStock()).isEqualTo(10);
    }

    @Test
    void should_decrease_normal_stock_only() {
        // Given: 일반 재고 10개, 프로모션 재고 10개
        Stock stock = createStock(10, 10);

        // When: 일반 재고 3개만 차감
        stock.decrease(0, 3);

        // Then: 일반 재고만 감소
        assertThat(stock.getNormalStock()).isEqualTo(7);
        assertThat(stock.getPromotionStock()).isEqualTo(10);
    }

    @Test
    void should_decrease_both_stocks() {
        // Given: 일반 재고 10개, 프로모션 재고 10개
        Stock stock = createStock(10, 10);

        // When: 프로모션 5개, 일반 3개 차감
        stock.decrease(5, 3);

        // Then: 양쪽 재고 모두 감소
        assertThat(stock.getPromotionStock()).isEqualTo(5);
        assertThat(stock.getNormalStock()).isEqualTo(7);
    }

    @Test
    void should_decrease_stock_to_zero() {
        // Given: 일반 재고 5개, 프로모션 재고 5개
        Stock stock = createStock(5, 5);

        // When: 모든 재고 차감
        stock.decrease(5, 5);

        // Then: 재고 0으로 감소
        assertThat(stock.getPromotionStock()).isEqualTo(0);
        assertThat(stock.getNormalStock()).isEqualTo(0);
    }

    @Test
    void should_return_total_stock() {
        // Given: 일반 재고 7개, 프로모션 재고 3개
        Stock stock = createStock(7, 3);

        // When: 전체 재고 확인
        int totalStock = stock.getTotalStock();

        // Then: 10개
        assertThat(totalStock).isEqualTo(10);
    }

    @Test
    void should_return_true_when_has_promotion_stock() {
        // Given: 프로모션 재고 3개 있음
        Stock stock = createStock(5, 3);

        // When: 프로모션 재고 존재 확인
        boolean hasPromotionStock = stock.hasPromotionStock();

        // Then: true 반환
        assertThat(hasPromotionStock).isTrue();
    }

    // Fixture 헬퍼 메서드
    private Stock createStock(int normalStock, int promotionStock) {
        return new Stock(normalStock, promotionStock);
    }
}
