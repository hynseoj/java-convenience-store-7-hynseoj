package store.model.promotion;

import store.model.Product;
import store.model.dto.PromotionResult;

public interface PromotionStrategy {

    PromotionResult applyPromotion(Product product, int quantity);
}
