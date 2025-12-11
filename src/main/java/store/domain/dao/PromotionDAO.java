package store.domain.dao;

import store.domain.Promotion;

public record PromotionDAO(Promotion promotion, int toBuy, int toProvide) {

}
