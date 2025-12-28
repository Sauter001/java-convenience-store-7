package store.domain.promotion;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BuyGetQuantityTest {

    @Test
    void should_calculate_set_size_for_2_plus_1() {
        // Given: 2+1 프로모션 (2개 구매 시 1개 증정)
        BuyGetQuantity buyGetQuantity = new BuyGetQuantity(2, 1);

        // When: 세트 크기 계산
        int setSize = buyGetQuantity.getSetSize();

        // Then: 2 + 1 = 3
        assertThat(setSize).isEqualTo(3);
    }

    @Test
    void should_calculate_set_size_for_1_plus_1() {
        // Given: 1+1 프로모션 (1개 구매 시 1개 증정)
        BuyGetQuantity buyGetQuantity = new BuyGetQuantity(1, 1);

        // When: 세트 크기 계산
        int setSize = buyGetQuantity.getSetSize();

        // Then: 1 + 1 = 2
        assertThat(setSize).isEqualTo(2);
    }

    @Test
    void should_calculate_set_size_for_3_plus_2() {
        // Given: 3+2 프로모션 (3개 구매 시 2개 증정) - 엣지 케이스
        BuyGetQuantity buyGetQuantity = new BuyGetQuantity(3, 2);

        // When: 세트 크기 계산
        int setSize = buyGetQuantity.getSetSize();

        // Then: 3 + 2 = 5
        assertThat(setSize).isEqualTo(5);
    }

    @Test
    void should_create_buy_get_quantity_with_valid_values() {
        // Given: requiredQuantity=2, bonusQuantity=1
        int requiredQuantity = 2;
        int bonusQuantity = 1;

        // When: BuyGetQuantity 생성
        BuyGetQuantity buyGetQuantity = new BuyGetQuantity(requiredQuantity, bonusQuantity);

        // Then: 필드 값 검증
        assertThat(buyGetQuantity.requiredQuantity()).isEqualTo(2);
        assertThat(buyGetQuantity.bonusQuantity()).isEqualTo(1);
    }
}
