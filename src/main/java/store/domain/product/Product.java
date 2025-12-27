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

    public String getName() {
        return name;
    }

    public boolean hasPromotion() {
        return promotion != null && !promotion.isExpired();
    }

    public List<ProductDisplayDto> toDisplayDtos() {
        List<ProductDisplayDto> dtos = new ArrayList<>();
        if (hasPromotion() && stock.getPromotionStock() > 0) {
            addPromotionDisplayDtos(dtos);
        }
        if (!hasPromotion() || stock.getPromotionStock() == 0) {
            addNormalDisplayDto(dtos);
        }
        return dtos;
    }

    private void addPromotionDisplayDtos(List<ProductDisplayDto> dtos) {
        dtos.add(new ProductDisplayDto(this.name, this.price, this.stock.getPromotionStock(), this.promotion.getName()));
        dtos.add(new ProductDisplayDto(this.name, this.price, this.stock.getNormalStock(), null));
    }

    private void addNormalDisplayDto(List<ProductDisplayDto> dtos) {
        int totalStock = stock.getPromotionStock() + stock.getNormalStock();
        dtos.add(new ProductDisplayDto(this.name, this.price, totalStock, null));
    }

    public StockState getStockState(int quantity) {
        return this.stock.checkStockState(quantity);
    }

    public PromotionConfirmation getPromotionConfirmation(int quantity) {
        StockState state = getStockState(quantity);
        if (state == StockState.INSUFFICIENT) {
            throw new StockExceededException();
        }
        if (shouldReturnFullyApplicable(state)) {
            return createFullyApplicable(quantity);
        }
        return createPartiallyApplicable(quantity);
    }

    private boolean shouldReturnFullyApplicable(StockState state) {
        return state == StockState.PROMOTION_ONLY || !hasPromotion();
    }

    private PromotionConfirmation createFullyApplicable(int quantity) {
        return new PromotionConfirmation.FullyApplicable(name, quantity);
    }

    private PromotionConfirmation createPartiallyApplicable(int quantity) {
        int promotionQuantity = stock.getPromotionStock();
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
}
