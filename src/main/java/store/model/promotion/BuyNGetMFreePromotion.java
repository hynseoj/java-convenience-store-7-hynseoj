package store.model.promotion;

import store.model.Product;
import store.model.dto.PromotionResult;

public class BuyNGetMFreePromotion implements PromotionStrategy {

    private final int purchaseQuantity;
    private final int freeQuantity;

    public BuyNGetMFreePromotion(int purchaseQuantity, int freeQuantity) {
        this.purchaseQuantity = purchaseQuantity;
        this.freeQuantity = freeQuantity;
    }

    @Override
    public PromotionResult applyPromotion(Product product, int quantity) {
        int totalFreeItemCount = (quantity / (purchaseQuantity + freeQuantity)) * freeQuantity;
        return PromotionResult.of(product.price() * totalFreeItemCount, totalFreeItemCount);
    }
}