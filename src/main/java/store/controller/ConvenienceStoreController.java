package store.controller;

import store.domain.Product;
import store.service.ProductService;
import store.view.InputView;
import store.view.OutputView;

import java.util.List;

public class ConvenienceStoreController {
    private final InputView inputView;
    private final OutputView outputView;
    private final ProductService productService;

    public ConvenienceStoreController(InputView inputView, OutputView outputView, ProductService productService) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.productService = productService;
    }

    public void run() {
        List<Product> products = productService.readProducts();
        outputView.displayStocks(products);
    }
}
