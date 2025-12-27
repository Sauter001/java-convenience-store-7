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

    public boolean hasPromotion() {
        return promotion != null && !promotion.isExpired();
    }

    public List<ProductDisplayDto> toDisplayDtos() {
        List<ProductDisplayDto> dtos = new ArrayList<>();
        if (hasPromotion() && stock.getPromotionStock() > 0) {
            dtos.add(new ProductDisplayDto(this.name, this.price, this.stock.getPromotionStock(), this.promotion.getName()));
        }
        dtos.add(
                new ProductDisplayDto(this.name, this.price, this.stock.getNormalStock(), null)
        );
        return dtos;
    }

    public StockState getStockState(int quantity) {
        return this.stock.checkStockState(quantity);
    }

    public PromotionConfirmation getPromotionConfirmation(int quantity) {
        StockState state = getStockState(quantity);

        if (state == StockState.INSUFFICIENT) {
            throw new StockExceededException();
        }
        if (state == StockState.PROMOTION_ONLY) {
            return createFullyApplicable(quantity);
        }
        return createPartiallyApplicable(quantity);
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
}
