package store.view;

import store.domain.order.Receipt;
import store.domain.order.dto.OrderPresentedDto;
import store.domain.order.dto.OrderReceiptDto;
import store.domain.product.dto.ProductDisplayDto;
import store.exception.ServiceException;

import java.text.DecimalFormat;
import java.util.List;

public class OutputView {

    public static final String HEADER_LINE = "=";

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

    private String displayPromotionName(String promotionName) {
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

    private String formatDiscount(int amount) {
        if (amount == 0) {
            return "-0";
        }
        return getDecimalFormat(-amount);
    }

    public void displayReceipt(Receipt receipt) {
        displayReceiptHeader("W 편의점");
        displayOrders(receipt.purchasedItems());
        displayReceiptHeader("증   정");
        displayPresents(receipt.presentedItems());
        displayGuideline();
        displayDiscount(receipt.paymentAmount(), receipt.totalQuantity());
    }

    private void displayDiscount(Receipt.PaymentAmount paymentAmount, int totalQuantity) {
        System.out.printf("총구매액\t\t\t%3d%10s\n", totalQuantity, getDecimalFormat(paymentAmount.totalAmount()));
        System.out.printf("행사할인\t\t\t\t%10s\n", formatDiscount(paymentAmount.promotionDiscount()));
        System.out.printf("멤버십할인\t\t\t%10s\n", formatDiscount(paymentAmount.membershipDiscount()));
        System.out.printf("내실돈\t\t\t%10s\n", getDecimalFormat(paymentAmount.calculateFinalAmount()));
    }

    private void displayPresents(List<OrderPresentedDto> orderPresentedDtos) {
        for (OrderPresentedDto orderPresentedDto : orderPresentedDtos) {
            System.out.printf("%-13s%3d\n", orderPresentedDto.productName(), orderPresentedDto.quantity());
        }
    }

    private void displayOrders(List<OrderReceiptDto> orderReceiptDtos) {
        System.out.println("상품명\t\t\t수량\t\t금액");
        for (OrderReceiptDto orderReceiptDto : orderReceiptDtos) {
            System.out.printf("%-13s%3d\t\t%-10s\n",
                    orderReceiptDto.productName(),
                    orderReceiptDto.quantity(),
                    getDecimalFormat(orderReceiptDto.cost())
            );
        }
    }

    private void displayReceiptHeader(String title) {
        String header = String.format("%s%s%s", HEADER_LINE.repeat(11), title, HEADER_LINE.repeat(13));
        System.out.println(header);
    }

    private void displayGuideline() {
        System.out.println(HEADER_LINE.repeat(29));
    }
}
