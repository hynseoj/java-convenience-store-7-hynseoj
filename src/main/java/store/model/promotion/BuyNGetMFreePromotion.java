package store.model.promotion;

import static store.common.constant.PromotionNotice.DEFAULT_NOTICE;
import static store.common.constant.PromotionNotice.GET_FREE_M_NOTICE;

import store.common.dto.PromotionConditionResult;
import store.common.dto.PromotionResult;
import store.model.Product;

public class BuyNGetMFreePromotion implements PromotionStrategy {

    private final int purchaseQuantity;
    private final int freeQuantity;

    public BuyNGetMFreePromotion(int purchaseQuantity, int freeQuantity) {
        this.purchaseQuantity = purchaseQuantity;
        this.freeQuantity = freeQuantity;
    }

    @Override
    public PromotionResult applyPromotion(Product product, int quantity) {
        int totalFreeQuantity = (quantity / (purchaseQuantity + freeQuantity)) * freeQuantity;
        return PromotionResult.of(product.price() * totalFreeQuantity, totalFreeQuantity);
    }

    @Override
    public PromotionConditionResult checkCondition(Product product, int quantity) {
        String message = DEFAULT_NOTICE.message();

        if (quantity % (purchaseQuantity + freeQuantity) == purchaseQuantity) {
            message = GET_FREE_M_NOTICE.message(product.name(), freeQuantity);
        }
        return new PromotionConditionResult(message);
    }
}
