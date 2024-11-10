package store.model.promotion;

import store.common.dto.PromotionConditionResult;
import store.common.dto.PromotionResult;
import store.model.Product;

public interface PromotionStrategy {

    PromotionResult applyPromotion(Product product, int quantity);

    PromotionConditionResult checkCondition(Product product, int quantity);
}
