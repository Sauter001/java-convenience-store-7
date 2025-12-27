package store.service;

import store.domain.product.Product;
import store.domain.product.ProductData;
import store.domain.product.Stock;
import store.domain.promotion.Promotion;
import store.exception.ServiceException;
import store.repository.ProductRepository;
import store.repository.PromotionRepository;

import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;

    public OrderService(ProductRepository productRepository, PromotionRepository promotionRepository) {
        this.productRepository = productRepository;
        this.promotionRepository = promotionRepository;
    }

    public List<Product> getProducts() {
        List<ProductData> dataList = productRepository.findAll();
        List<Product> products = new ArrayList<>();

        for (ProductData data : dataList) {
            products.add(convertDatatoProduct(data));
        }
        return products;
    }

    private Product convertDatatoProduct(ProductData data) {
        Promotion promotion = resolvePromotion(data.promotionName());
        Stock stock = createStock(data.quantity(), promotion);
        return new Product(data.name(), data.price(), stock, promotion);
    }

    private Promotion resolvePromotion(String promotionName) {
        if ("null".equals(promotionName)) {
            return null;
        }

        Promotion promotion = promotionRepository.findByName(promotionName);
        if (promotion == null) {
            throw new ServiceException("존재하지 않는 프로모션: " + promotionName);
        }
        return promotion;
    }

    private Stock createStock(int quantity, Promotion promotion) {
        if (promotion == null) {
            return new Stock(quantity, 0);
        }
        return new Stock(0, quantity);
    }
}
