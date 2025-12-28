package store;

import camp.nextstep.edu.missionutils.test.NsTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static camp.nextstep.edu.missionutils.test.Assertions.assertNowTest;
import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.assertj.core.api.Assertions.assertThat;

class ApplicationTest extends NsTest {
    @Test
    void should_print_product_list_from_file() {
        assertSimpleTest(() -> {
            run("[물-1]", "N", "N");
            assertThat(output()).contains(
                "- 콜라 1,000원 10개 탄산2+1",
                "- 콜라 1,000원 10개",
                "- 사이다 1,000원 8개 탄산2+1",
                "- 사이다 1,000원 7개",
                "- 오렌지주스 1,800원 9개 MD추천상품",
                "- 오렌지주스 1,800원 재고 없음",
                "- 탄산수 1,200원 5개 탄산2+1",
                "- 탄산수 1,200원 재고 없음",
                "- 물 500원 10개",
                "- 비타민워터 1,500원 6개",
                "- 감자칩 1,500원 5개 반짝할인",
                "- 감자칩 1,500원 5개",
                "- 초코바 1,200원 5개 MD추천상품",
                "- 초코바 1,200원 5개",
                "- 에너지바 2,000원 5개",
                "- 정식도시락 6,400원 8개",
                "- 컵라면 1,700원 1개 MD추천상품",
                "- 컵라면 1,700원 10개"
            );
        });
    }

    @Test
    void should_purchase_multiple_normal_products() {
        assertSimpleTest(() -> {
            run("[비타민워터-3],[물-2],[정식도시락-2]", "N", "N");
            assertThat(output().replaceAll("\\s", "")).contains("내실돈18,300");
        });
    }

    @Test
    void should_not_apply_promotion_outside_period() {
        assertNowTest(() -> {
            run("[감자칩-2]", "N", "N");
            assertThat(output().replaceAll("\\s", "")).contains("내실돈3,000");
        }, LocalDate.of(2024, 2, 1).atStartOfDay());
    }

    @Test
    void should_throw_exception_test() {
        assertSimpleTest(() -> {
            runException("[컵라면-12]", "N", "N");
            assertThat(output()).contains("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        });
    }

    // 새 테스트 코드들

    @Test
    void should_apply_full_promotion_and_show_presented_items() {
        // 콜라 6개 구매 (2+1 프로모션 완전 적용)
        // 예상: 증정 2개, 프로모션 할인 2000원
        assertSimpleTest(() -> {
            run("[콜라-6]", "N", "N");
            assertThat(output().replaceAll("\\s", ""))
                .contains("콜라2")
                .contains("행사할인-2,000")
                .contains("내실돈4,000");
        });
    }

    @Test
    void should_suggest_additional_item_for_promotion() {
        // 콜라 2개 구매 (2+1 프로모션, 1개 더 받을 수 있음)
        // "현재 콜라은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)"
        assertSimpleTest(() -> {
            run("[콜라-2]", "Y", "N", "N");
            assertThat(output())
                .contains("1개를 무료로 더 받을 수 있습니다")
                .contains("추가하시겠습니까?");
        });
    }

    @Test
    void should_apply_membership_discount() {
        // 에너지바 5개 (프로모션 없음, 10000원)
        // 멤버십 30% 할인 → 3000원
        assertSimpleTest(() -> {
            run("[에너지바-5]", "Y", "N");
            assertThat(output().replaceAll("\\s", ""))
                .contains("멤버십할인-3,000")
                .contains("내실돈7,000");
        });
    }

    @Test
    void should_limit_membership_discount_to_8000() {
        // 정식도시락 8개 (51200원, 프로모션 없음)
        // 멤버십 30% → 15360원이지만 8000원 한도
        assertSimpleTest(() -> {
            run("[정식도시락-8]", "Y", "N");
            assertThat(output().replaceAll("\\s", ""))
                .contains("멤버십할인-8,000")
                .contains("내실돈43,200");
        });
    }

    @Test
    void should_apply_both_promotion_and_membership_discount() {
        // 콜라 6개 (2+1, 프로모션 할인 2000) + 물 10개 (5000원, 멤버십 대상)
        // 멤버십 할인: 5000 * 0.3 = 1500
        assertSimpleTest(() -> {
            run("[콜라-6],[물-10]", "Y", "N");
            assertThat(output().replaceAll("\\s", ""))
                .contains("행사할인-2,000")
                .contains("멤버십할인-1,500");
        });
    }

    @Override
    public void runMain() {
        Application.main(new String[]{});
    }
}
