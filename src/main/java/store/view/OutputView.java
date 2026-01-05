package store.view;

import store.domain.product.dto.ProductOverviewDto;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

public class OutputView {
    public void displayError(Exception e) {
        System.out.println(e.getMessage());
    }

    public void displayProducts(List<ProductOverviewDto> overviewDtos) {
        System.out.println("안녕하세요. W편의점입니다.");
        System.out.println("현재 보유하고 있는 상품입니다.");
        System.out.println();
        for (ProductOverviewDto dto : overviewDtos) {
            displaySingleProduct(dto);
        }
    }

    private void displaySingleProduct(ProductOverviewDto productOverviewDto) {
        System.out.printf("- %s %s원 %s %s%n",
                productOverviewDto.productName(),
                getPriceFormat(productOverviewDto.price()),
                getStockContent(productOverviewDto.quantity()),
                getPromotionName(productOverviewDto.promotionName())
        );
    }

    private static String getStockContent(int quantity) {
        if (isSoldOut(quantity)) {
            return "재고 없음";
        }
        return quantity + "개";
    }

    private static boolean isSoldOut(int quantity) {
        return quantity == 0;
    }

    private String  getPromotionName(String promotionName) {
        if (Objects.isNull(promotionName)) {
            return "";
        }
        return promotionName;
    }

    private String getPriceFormat(int price) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        return decimalFormat.format(price);
    }
}
