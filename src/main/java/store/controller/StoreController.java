package store.controller;

import store.domain.product.Products;
import store.exception.ServiceException;
import store.service.StoreService;
import store.view.InputView;
import store.view.OutputView;

import java.util.List;
import java.util.function.Supplier;

public class StoreController {
    private final InputView inputView;
    private final OutputView outputView;
    private final StoreService storeService;

    public StoreController(InputView inputView, OutputView outputView, StoreService storeService) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.storeService = storeService;
    }

    public void run() {
        Products products = storeService.findAllProducts();
        outputView.showStockInfo(products.toAllDisplayDtos());
    }

    private void retry(Runnable task) {
        while (true) {
            try {
                task.run();
                return;
            } catch (ServiceException e) {
                outputView.printError(e);
            }
        }
    }

    private <T> T retry(Supplier<T> task) {
        while (true) {
            try {
                return task.get();
            } catch (ServiceException e) {
                outputView.printError(e);
            }
        }
    }
}
