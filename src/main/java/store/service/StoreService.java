package store.service;

import store.domain.order.Order;
import store.domain.order.Orders;
import store.domain.order.Receipt;
import store.domain.order.dto.OrderForm;
import store.domain.product.Product;
import store.domain.product.Products;

import java.util.List;

public class StoreService {
    private final OrderService orderService;
    private final PromotionService promotionService;
    private final DiscountService discountService;

    public StoreService(OrderService orderService, PromotionService promotionService, DiscountService discountService) {
        this.orderService = orderService;
        this.promotionService = promotionService;
        this.discountService = discountService;
    }

    public Products findAllProducts() {
        return new Products(orderService.getProducts());
    }

    public Orders convertToOrders(List<OrderForm> orderForms) {
        List<Order> orders = orderForms.stream()
                .map(this::makeOrderFromForm)
                .toList();
        return new Orders(orders);
    }

    private Order makeOrderFromForm(OrderForm orderForm) {
        Product product = orderService.findProductByName(orderForm.productName());
        return new Order(product, orderForm.quantity());
    }

    public Receipt createReceipt(Orders orders, boolean isMembership) {
        return discountService.createReceipt(orders, isMembership);
    }

    public void processStock(Orders orders) {
        for (Order order : orders) {
            Product product = orderService.findProductByName(order.getProductName());
            product.decreaseStock(order.getQuantity());
        }
    }
}
