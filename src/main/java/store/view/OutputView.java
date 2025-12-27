package store.view;

import store.domain.product.dto.ProductDisplayDto;
import store.exception.ServiceException;

import java.text.DecimalFormat;
import java.util.List;

public class OutputView {
    public void printError(ServiceException e) {
        System.out.println(e.getMessage());
    }

    public void showStockInfo(List<ProductDisplayDto> productDtos) {
        System.out.println("안녕하세요. W편의점입니다.");
        System.out.println("현재 보유하고 있는 상품입니다.\n");
        for (ProductDisplayDto productDto : productDtos) {
            displayProduct(productDto);
        }
        System.out.println();
    }

    private void displayProduct(ProductDisplayDto productDto) {
        String format = "- %s %s원 %s %s\n";
        System.out.printf(format, productDto.productName(), getDecimalFormat(productDto.price()),
                displayQuantity(productDto.stockQuantity()), displayPromotionName(productDto.promotionName()));
    }

    private  String displayPromotionName(String promotionName) {
        if (promotionName == null) {
            return "";
        }
        return promotionName;
    }

    private String displayQuantity(int quantity) {
        if (quantity == 0) {
            return "재고 없음";
        }

        return quantity + "개";
    }

    private String getDecimalFormat(int number) {
        DecimalFormat df = new DecimalFormat("#,##0");
        return df.format(number);
    }
}
