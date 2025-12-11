package store;

import store.config.AppConfig;
import store.controller.ConvenienceStoreController;
import store.repository.ProductRepository;
import store.service.ProductService;
import store.view.InputView;
import store.view.OutputView;

public class Application {
    public static void main(String[] args) {
        AppConfig appConfig = new AppConfig();
        InputView inputView = new InputView();
        OutputView outputView = new OutputView();
        ProductService productService = appConfig.createProductService();

        ConvenienceStoreController convenienceStoreController = new ConvenienceStoreController(inputView, outputView, productService);
        convenienceStoreController.run();
    }
}
