package store.service;

import store.domain.product.Product;
import store.domain.product.ProductData;
import store.domain.product.Stock;
import store.domain.promotion.Promotion;
import store.exception.ProductNotFoundException;
import store.exception.ServiceException;
import store.repository.ProductRepository;
import store.repository.PromotionRepository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderService {
    public static final String NULL_ATTR = "null";
    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;
    private List<Product> cachedProducts;

    public OrderService(ProductRepository productRepository, PromotionRepository promotionRepository) {
        this.productRepository = productRepository;
        this.promotionRepository = promotionRepository;
        this.cachedProducts = null;
    }

    public List<Product> getProducts() {
        if (Objects.isNull(this.cachedProducts)) {
            List<ProductData> dataList = productRepository.findAll();
            cachedProducts = groupByProductName(dataList);
        }
        return cachedProducts;
    }

    private List<Product> groupByProductName(List<ProductData> dataList) {
        Map<String, List<ProductData>> grouped = dataList.stream()
                .collect(Collectors.groupingBy(ProductData::name));

        return grouped.values().stream()
                .map(this::createProductFromGroup)
                .toList();
    }

    private Product createProductFromGroup(List<ProductData> dataList) {
        if (hasTwoItems(dataList)) {
            return mergeProducts(dataList.get(0), dataList.get(1));
        }
        return convertDataToProduct(dataList.get(0));
    }

    private boolean hasTwoItems(List<ProductData> dataList) {
        return dataList.size() == 2;
    }

    private Product mergeProducts(ProductData first, ProductData second) {
        int normalStock = getNormalStock(first, second);
        int promotionStock = getPromotionStock(first, second);
        Promotion promotion = getActivePromotion(first, second);

        return new Product(first.name(), first.price(), new Stock(normalStock, promotionStock), promotion);
    }

    private int getNormalStock(ProductData first, ProductData second) {
        if (hasPromotion(first)) {
            return second.quantity();
        }
        return first.quantity();
    }

    private int getPromotionStock(ProductData first, ProductData second) {
        if (hasPromotion(first)) {
            return first.quantity();
        }
        return second.quantity();
    }

    private Promotion getActivePromotion(ProductData first, ProductData second) {
        if (hasPromotion(first)) {
            return resolvePromotion(first.promotionName());
        }
        return resolvePromotion(second.promotionName());
    }

    private boolean hasPromotion(ProductData data) {
        return !NULL_ATTR.equals(data.promotionName());
    }

    private Product convertDataToProduct(ProductData data) {
        Promotion promotion = resolvePromotion(data.promotionName());
        Stock stock = createStock(data.quantity(), promotion);
        return new Product(data.name(), data.price(), stock, promotion);
    }

    private Promotion resolvePromotion(String promotionName) {
        if (NULL_ATTR.equals(promotionName)) {
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

    public Product findProductByName(String productName) {
        return getProducts().stream()
                .filter(p -> p.getName().equals(productName))
                .findFirst()
                .orElseThrow(ProductNotFoundException::new);
    }
}
