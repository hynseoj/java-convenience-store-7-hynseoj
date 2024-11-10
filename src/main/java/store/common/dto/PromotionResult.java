package store.common.dto;

public record PromotionResult(
        int discountAmount,
        int freeQuantity
) {
    public static PromotionResult of(int discountAmount, int freeQuantity) {
        return new PromotionResult(discountAmount, freeQuantity);
    }
}
