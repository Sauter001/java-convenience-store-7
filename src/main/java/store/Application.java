package store;

import store.config.AppConfig;
import store.controller.StoreController;
import store.view.InputView;
import store.view.OutputView;

public class Application {
    public static void main(String[] args) {
        AppConfig config = new AppConfig();
        InputView inputView = new InputView();
        OutputView outputView = new OutputView();

        StoreController storeController = new StoreController(inputView, outputView, config.createStoreService());
        storeController.run();
    }
}
