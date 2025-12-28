package store.domain.product;

import org.junit.jupiter.api.Test;
import store.domain.product.dto.ProductDisplayDto;
import store.domain.promotion.BuyGetQuantity;
import store.domain.promotion.Promotion;
import store.domain.promotion.PromotionPeriod;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductsTest {

    @Test
    void should_flatten_all_product_display_dtos() {
        // Given:
        //   - Product1 (프로모션 있음): 2개 DTO 반환
        //   - Product2 (프로모션 없음): 1개 DTO 반환
        Product product1 = createProductWithPromotion("콜라", 1000, 5, 10);
        Product product2 = createProductWithoutPromotion("물", 500, 10);
        Products products = createProducts(product1, product2);

        // When: DTO 리스트로 변환
        List<ProductDisplayDto> dtos = products.toAllDisplayDtos();

        // Then: 총 3개 DTO (flatMap으로 평탄화)
        assertThat(dtos).hasSize(3);
    }

    @Test
    void should_return_correct_dto_order_for_promotion_products() {
        // Given: 프로모션 있는 상품
        Product product = createProductWithPromotion("콜라", 1000, 5, 10);
        Products products = createProducts(product);

        // When: DTO 리스트로 변환
        List<ProductDisplayDto> dtos = products.toAllDisplayDtos();

        // Then: 첫 번째 DTO는 프로모션재고, 두 번째 DTO는 일반재고
        assertThat(dtos).hasSize(2);
        assertThat(dtos.get(0).stockQuantity()).isEqualTo(10); // 프로모션 재고
        assertThat(dtos.get(0).promotionName()).isEqualTo("탄산2+1");
        assertThat(dtos.get(1).stockQuantity()).isEqualTo(5);  // 일반 재고
        assertThat(dtos.get(1).promotionName()).isNull();
    }

    @Test
    void should_handle_multiple_promotion_products() {
        // Given: 3개 상품 모두 프로모션 있음
        Product product1 = createProductWithPromotion("콜라", 1000, 5, 10);
        Product product2 = createProductWithPromotion("사이다", 1000, 7, 8);
        Product product3 = createProductWithPromotion("탄산수", 1200, 0, 5);
        Products products = createProducts(product1, product2, product3);

        // When: DTO 리스트로 변환
        List<ProductDisplayDto> dtos = products.toAllDisplayDtos();

        // Then: 6개 DTO (각 상품당 2개씩)
        assertThat(dtos).hasSize(6);
    }

    @Test
    void should_handle_multiple_normal_products() {
        // Given: 3개 상품 모두 프로모션 없음
        Product product1 = createProductWithoutPromotion("콜라", 1000, 10);
        Product product2 = createProductWithoutPromotion("물", 500, 20);
        Product product3 = createProductWithoutPromotion("에너지바", 2000, 5);
        Products products = createProducts(product1, product2, product3);

        // When: DTO 리스트로 변환
        List<ProductDisplayDto> dtos = products.toAllDisplayDtos();

        // Then: 3개 DTO
        assertThat(dtos).hasSize(3);
    }

    @Test
    void should_return_empty_list_when_no_products() {
        // Given: 빈 Products
        Products products = createProducts();

        // When: DTO 리스트로 변환
        List<ProductDisplayDto> dtos = products.toAllDisplayDtos();

        // Then: 빈 리스트
        assertThat(dtos).isEmpty();
    }

    @Test
    void should_iterate_over_products() {
        // Given: 3개 Product를 가진 Products
        Product product1 = createProductWithoutPromotion("콜라", 1000, 10);
        Product product2 = createProductWithoutPromotion("물", 500, 20);
        Product product3 = createProductWithoutPromotion("사이다", 1000, 15);
        Products products = createProducts(product1, product2, product3);

        // When: for-each로 순회
        List<Product> iteratedProducts = new ArrayList<>();
        for (Product product : products) {
            iteratedProducts.add(product);
        }

        // Then: 3번 순회
        assertThat(iteratedProducts).hasSize(3);
        assertThat(iteratedProducts.get(0).getName()).isEqualTo("콜라");
        assertThat(iteratedProducts.get(1).getName()).isEqualTo("물");
        assertThat(iteratedProducts.get(2).getName()).isEqualTo("사이다");
    }

    // Fixture 헬퍼 메서드
    private Products createProducts(Product... products) {
        return new Products(List.of(products));
    }

    private Product createProductWithPromotion(String name, int price, int normalStock, int promotionStock) {
        Stock stock = new Stock(normalStock, promotionStock);
        Promotion promotion = create2Plus1Promotion();
        return new Product(name, price, stock, promotion);
    }

    private Product createProductWithoutPromotion(String name, int price, int stock) {
        Stock stockObj = new Stock(stock, 0);
        return new Product(name, price, stockObj, null);
    }

    private Promotion create2Plus1Promotion() {
        BuyGetQuantity buyGetQuantity = new BuyGetQuantity(2, 1);
        PromotionPeriod period = new PromotionPeriod(
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(30)
        );
        return new Promotion("탄산2+1", buyGetQuantity, period);
    }
}
