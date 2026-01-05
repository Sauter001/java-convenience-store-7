package store.service;

import store.domain.order.Order;
import store.domain.order.Orders;
import store.domain.order.dto.OrderForm;
import store.domain.product.Product;
import store.domain.product.Products;
import store.repository.ProductRepository;
import store.repository.PromotionRepository;

import java.util.ArrayList;
import java.util.List;

public class StoreService {
    private final PromotionRepository promotionRepository;
    private final ProductRepository productRepository;

    public StoreService(PromotionRepository promotionRepository, ProductRepository productRepository) {
        this.promotionRepository = promotionRepository;
        this.productRepository = productRepository;
    }

    public Products readProducts() {
        return new Products(productRepository.findAll());
    }

    public Orders convertToOrders(List<OrderForm> orderForms) {
        List<Order> orderList = new ArrayList<>();
        for (OrderForm form : orderForms) {
            Product product = productRepository.findByName(form.productName());
            Order order = new Order(product, form.quantity());
            orderList.add(order);
        }
        return new Orders(orderList);
    }

    public Orders findAddibleOrders(Orders orders) {
    }
}
