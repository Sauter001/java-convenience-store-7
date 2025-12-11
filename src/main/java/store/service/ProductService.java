package store.service;

import store.domain.Order;
import store.domain.Product;
import store.domain.StockTarget;
import store.domain.dao.PromotionDAO;
import store.domain.state.StockState;
import store.exception.ErrorMessage;
import store.exception.ServiceException;
import store.repository.ProductRepository;
import store.repository.PromotionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductService {
    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;

    public ProductService(ProductRepository productRepository, PromotionRepository promotionRepository) {
        this.productRepository = productRepository;
        this.promotionRepository = promotionRepository;
    }

    public List<Product> readProducts() {
        return productRepository.findAll();
    }

    public void validateOrders(List<Order> orders) {
        validateStock(orders);
        validateProductExistence(orders);
    }

    public List<StockTarget> getStockStates(List<Order> orders) {
        List<StockTarget> stockTargets = new ArrayList<>();
        for (Order order : orders) {
            stockTargets.add(determineStockState(order));
        }

        return stockTargets;
    }

    private StockTarget determineStockState(Order order) {
        List<Product> products = this.productRepository.findByName(order.productName());
        Optional<Product> optionalPromotionProduct = products.stream().filter(Product::hasPromotion).findFirst();
        Optional<Product> optionalDefaultProduct = products.stream().filter(p -> !p.hasPromotion()).findFirst();

        if (optionalPromotionProduct.isPresent()) {
            Product promotionProduct = optionalPromotionProduct.get();
            return findStockWhenPromotion(promotionProduct, order);
        }

        if (optionalDefaultProduct.isPresent()) {
            return new StockTarget(StockState.PROMOTION_NOT_ENOUGH, order);
        }

        return new StockTarget(StockState.SOLD_OUT, order);
    }

    private StockTarget findStockWhenPromotion(Product promotionProduct, Order order) {
        int quantity = order.quantity();
        PromotionDAO promotionDAO = this.promotionRepository.findByPromotion(promotionProduct.promotion());

        if (promotionDAO.toBuy() > quantity) {
            return new StockTarget(StockState.PROMOTION_ENOUGH_ORDER_SHORT, order);
        }

        return new StockTarget(StockState.PROMOTION_ENOUGH, order);
    }

    private void validateStock(List<Order> orders) {
        // 구매 수량이 재고 수량(프로모션+일반) 초과
        for (Order order : orders) {
            int boughtQuantity = order.quantity();
            int stock = this.productRepository.getStockOf(order.productName());

            if (stock < boughtQuantity) {
                throw new ServiceException(ErrorMessage.SOLD_OUT);
            }
        }
    }

    private void validateProductExistence(List<Order> orders) {
        boolean hasProduct = orders.stream()
                .allMatch(o -> this.productRepository.existProductOfName(o.productName()));

        if (!hasProduct) {
            throw new ServiceException(ErrorMessage.PRODUCT_NOT_EXIST);
        }
    }
}
