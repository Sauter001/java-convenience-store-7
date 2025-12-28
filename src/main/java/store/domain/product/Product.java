package store.domain.product;

import store.domain.order.dto.PromotionConfirmation;
import store.domain.product.dto.ProductDisplayDto;
import store.domain.promotion.Promotion;
import store.exception.StockExceededException;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private final String name;
    private final int price;
    private final Stock stock;
    private final Promotion promotion;

    public Product(String name, int price, Stock stock) {
        this(name, price, stock, null);
    }

    public Product(String name, int price, Stock stock, Promotion promotion) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.promotion = promotion;
    }

    private static void validateStock(StockState state) {
        if (state == StockState.INSUFFICIENT) {
            throw new StockExceededException();
        }
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public boolean hasPromotion() {
        return promotion != null && !promotion.isExpired();
    }

    public List<ProductDisplayDto> toDisplayDtos() {
        List<ProductDisplayDto> dtos = new ArrayList<>();
        if (hasPromotion() && stock.hasPromotionStock()) {
            addPromotionDisplayDtos(dtos);
        }
        if (!hasPromotion() || !stock.hasPromotionStock()) {
            addNormalDisplayDto(dtos);
        }
        return dtos;
    }

    private void addPromotionDisplayDtos(List<ProductDisplayDto> dtos) {
        dtos.add(new ProductDisplayDto(this.name, this.price, this.stock.getPromotionStock(), this.promotion.getName()));
        dtos.add(new ProductDisplayDto(this.name, this.price, this.stock.getNormalStock(), null));
    }

    private void addNormalDisplayDto(List<ProductDisplayDto> dtos) {
        int totalStock = stock.getTotalStock();
        dtos.add(new ProductDisplayDto(this.name, this.price, totalStock, null));
    }

    public StockState getStockState(int quantity) {
        return this.stock.checkStockState(quantity);
    }

    public PromotionConfirmation getPromotionConfirmation(int quantity) {
        StockState state = getStockState(quantity);
        validateStock(state);
        if (!hasPromotion()) {
            return createNoPromotion(quantity);
        }
        return determinePromotionType(quantity, state);
    }

    private PromotionConfirmation determinePromotionType(int quantity, StockState state) {
        if (shouldReturnFullyApplicable(state)) {
            return createFullyApplicable(quantity);
        }
        return createPartiallyApplicable(quantity);
    }

    private PromotionConfirmation createNoPromotion(int quantity) {
        return new PromotionConfirmation.NoPromotion(this.name, quantity);
    }

    private boolean shouldReturnFullyApplicable(StockState state) {
        return state == StockState.PROMOTION_ONLY;
    }

    private PromotionConfirmation createFullyApplicable(int quantity) {
        return new PromotionConfirmation.FullyApplicable(name, quantity);
    }

    private PromotionConfirmation createPartiallyApplicable(int quantity) {
        int promotionStock = stock.getPromotionStock();
        int setSize = promotion.getSetSize();

        int maxSets = promotionStock / setSize;
        int promotionQuantity = maxSets * setSize;
        int regularQuantity = quantity - promotionQuantity;

        return new PromotionConfirmation.PartiallyApplicable(
                name, promotionQuantity, regularQuantity
        );
    }

    public boolean shouldSuggestAdditionalItem(int quantity) {
        if (!hasPromotion()) {
            return false;
        }
        return promotion.shouldSuggestAdditional(quantity, stock.getPromotionStock());
    }

    public int getAdditionalQuantity(int quantity) {
        if (!shouldSuggestAdditionalItem(quantity)) {
            return 0;
        }
        return promotion.getBonusQuantity();
    }

    public int calculateFullAmount(int quantity) {
        return this.price * quantity;
    }

    public int getPresentedQuantity(int appliedQuantity) {
        int discount = calculatePromotionDiscount(appliedQuantity);
        if (discount == 0) {
            return 0;
        }
        return discount / this.price;
    }

    public int calculatePromotionDiscount(int appliedQuantity) {
        int freeQuantity = appliedQuantity / promotion.getSetSize();
        return freeQuantity * this.price;
    }

    public void decreaseStock(int quantity) {
        PromotionConfirmation confirmation = getPromotionConfirmation(quantity);
        if (decreaseStockByPromotion(confirmation)) {
            return;
        }
        stock.decrease(0, quantity);
    }

    private boolean decreaseStockByPromotion(PromotionConfirmation confirmation) {
        if (confirmation instanceof  PromotionConfirmation.FullyApplicable fully) {
            stock.decrease(fully.totalQuantity(), 0);
            return true;
        }
        if (confirmation instanceof PromotionConfirmation.PartiallyApplicable partial) {
            stock.decrease(partial.promotionQuantity(), partial.regularPriceQuantity());
            return true;
        }
        return false;
    }
}
