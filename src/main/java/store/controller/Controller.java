package store.controller;

import store.domain.io.BinaryResponse;
import store.domain.order.Orders;
import store.domain.order.dto.OrderForm;
import store.domain.product.Products;
import store.error.StoreException;
import store.service.StoreService;
import store.view.InputView;
import store.view.OutputView;

import java.util.List;
import java.util.function.Supplier;

public class Controller {
    private final StoreService storeService;
    private final InputView inputView;
    private final OutputView outputView;

    public Controller(StoreService storeService, InputView inputView, OutputView outputView) {
        this.storeService = storeService;
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void run() {
        while (true) {
            BinaryResponse response = retry(this::processPurchase);
            if (!response.isYes()) {
                break;
            }
        }
    }

    private BinaryResponse processPurchase() {
        displayProductInfo();
        retry(() -> {
            List<OrderForm> orderForms = inputView.readOrders();
            Orders orders = storeService.convertToOrders(orderForms);
        });
        return askContinue();
    }

    private void displayProductInfo() {
        Products products = storeService.readProducts();
        outputView.displayProducts(products.toOverviewDtos());
    }

    private BinaryResponse askContinue() {
        return inputView.readKeepBuying();
    }

    private void retry(Runnable task) {
        while (true) {
            try {
                task.run();
                return;
            } catch (StoreException e) {
                outputView.displayError(e);
            }
        }
    }

    private <T> T retry(Supplier<T> task) {
        while (true) {
            try {
                return task.get();
            } catch (StoreException e) {
                outputView.displayError(e);
            }
        }
    }
}
