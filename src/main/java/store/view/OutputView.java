package store.view;

import store.domain.Product;
import store.exception.ServiceException;

import java.text.DecimalFormat;
import java.util.List;

public class OutputView {
    private static final DecimalFormat priceFormat = new DecimalFormat("#,##0");

    public void displayStocks(List<Product> products) {
        System.out.println("안녕하세요. W편의점입니다.");
        System.out.println("현재 보유하고 있는 상품입니다.\n");
        for (Product product : products) {
            displaySingleStock(product);
        }
        displayBlank();
    }

    public void displayError(ServiceException serviceException) {
        System.out.println(serviceException.getMessage());
    }

    private static void displayBlank() {
        System.out.println();
    }

    private void displaySingleStock(Product product) {
        System.out.printf("- %s %s원 %s %s\n",
                product.name(),
                getFormattedNumber(product.price()),
                getStockInfo(product),
                product.getPromotionName());
    }

    private static String getStockInfo(Product product) {
        int stock = product.stock();
        if (product.stock() > 0) {
            return String.format("%d개", stock);
        }

        return "재고 없음";
    }

    private String getFormattedNumber(int number) {
        return priceFormat.format(number);
    }
}
