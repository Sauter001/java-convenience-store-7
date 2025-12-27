package store.domain.product;

public enum StockState {
    PROMOTION_ONLY,          // 프로모션 재고만 사용
    PROMOTION_WITH_NORMAL,   // 프로모션 + 일반 혼용
    INSUFFICIENT;            // 재고 부족
}
