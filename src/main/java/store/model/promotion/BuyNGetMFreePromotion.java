package store.model.promotion;

import static store.common.constant.PromotionNotice.DEFAULT_NOTICE;
import static store.common.constant.PromotionNotice.GET_FREE_M_NOTICE;
import static store.common.constant.PromotionNotice.OUT_OF_STOCK_NOTICE;

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
        int totalFreeQuantity = getApplicableSetCount(quantity) * freeQuantity;
        return PromotionResult.of(product.price() * totalFreeQuantity, totalFreeQuantity);
    }

    @Override
    public PromotionConditionResult checkCondition(Product product, int quantity) {
        int setCountFromRequest = getApplicableSetCount(quantity);
        int setCountFromStock = getApplicableSetCount(product.stock());
        int applicableSetCount = Math.min(setCountFromRequest, setCountFromStock);
        int stockUsed = applicableSetCount * getPromotionSetSize();
        int remainingQuantity = quantity - stockUsed;

        String message = getConditionMessage(product, product.stock() - stockUsed, remainingQuantity);
        return PromotionConditionResult.from(applicableSetCount, stockUsed, remainingQuantity, message);
    }

    private String getConditionMessage(Product product, int remainingStock, int remainingQuantity) {
        if (remainingQuantity == purchaseQuantity && remainingStock > purchaseQuantity) {
            return GET_FREE_M_NOTICE.message(product.name(), freeQuantity);
        }
        if (remainingQuantity >= getPromotionSetSize()) {
            return OUT_OF_STOCK_NOTICE.message(product.name(), remainingQuantity);
        }
        return DEFAULT_NOTICE.message();
    }

    private int getPromotionSetSize() {
        return purchaseQuantity + freeQuantity;
    }

    private int getApplicableSetCount(int quantity) {
        return quantity / getPromotionSetSize();
    }

    private int getRemainingQuantity(int quantity) {
        return quantity % getPromotionSetSize();
    }
}
