package store.controller;

import store.domain.io.BinaryResponse;
import store.domain.order.Order;
import store.domain.order.Orders;
import store.domain.order.dto.OrderForm;
import store.domain.order.dto.PromotionConfirmation;
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
        displayProducts();
        processOrderWithRetry();
    }

    private void displayProducts() {
        Products products = storeService.findAllProducts();
        outputView.showStockInfo(products.toAllDisplayDtos());
    }

    private void processOrderWithRetry() {
        retry(() -> {
            Orders orders = retry(this::convertFormToOrders);
            processOrders(orders);
            processMembership(orders);
        });
    }



    private Orders convertFormToOrders() {
        List<OrderForm> orderForms = inputView.readOrders();
        return storeService.convertToOrders(orderForms);
    }

    private void processOrders(Orders orders) {
        for (Order order : orders) {
            processOrder(order);
        }
    }

    private void processOrder(Order order) {
        checkAdditionalPromotion(order);
        checkPartialPromotion(order);
    }

    private void checkAdditionalPromotion(Order order) {
        if (!order.shouldSuggestAdditionalItem()) {
            return;
        }
        int additionalQty = order.getAdditionalQuantity();
        BinaryResponse response = inputView.confirmAdditionalItem(
                order.getProductName(), additionalQty
        );
        if (response == BinaryResponse.YES) {
            order.increaseQuantity(additionalQty);
        }
    }

    private void checkPartialPromotion(Order order) {
        PromotionConfirmation confirmation = order.getPromotionConfirmation();
        if (confirmation instanceof PromotionConfirmation.PartiallyApplicable partial) {
            handlePartialPromotion(order, partial);
        }
    }

    private void handlePartialPromotion(Order order, PromotionConfirmation.PartiallyApplicable partial) {
        BinaryResponse response = inputView.confirmPartialPromotion(partial);
        if (response == BinaryResponse.NO) {
            order.adjustQuantity(partial.promotionQuantity());
        }
    }

    private void processMembership(Orders orders) {
        BinaryResponse response = inputView.confirmMembership();
        processMembershipResponse(response, orders);
    }

    private void processMembershipResponse(BinaryResponse response, Orders orders) {
        if (response == BinaryResponse.NO) {
            return;
        }

        int memberShipDiscount = orders.calculateMembershipDiscount();
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
