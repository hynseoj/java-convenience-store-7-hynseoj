package store.common.dto;

public record PromotionResult(
        String productName,
        int discountAmount,
        int freeQuantity
) {
    public static PromotionResult of(String productName, int discountAmount, int freeQuantity) {
        return new PromotionResult(productName, discountAmount, freeQuantity);
    }
}
