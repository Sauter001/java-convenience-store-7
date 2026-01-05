package store;

import store.controller.Controller;
import store.repository.ProductRepository;
import store.repository.PromotionRepository;
import store.service.StoreService;
import store.view.InputView;
import store.view.OutputView;

public class Application {
    public static void main(String[] args) {
        StoreService storeService = createStoreService();
        InputView inputView = new InputView();
        OutputView outputView = new OutputView();
        Controller controller = new Controller(storeService, inputView, outputView);
        controller.run();
    }

    private static StoreService createStoreService() {
        PromotionRepository promotionRepository = new PromotionRepository();
        ProductRepository productRepository = new ProductRepository();
        return new StoreService(promotionRepository, productRepository);
    }
}
