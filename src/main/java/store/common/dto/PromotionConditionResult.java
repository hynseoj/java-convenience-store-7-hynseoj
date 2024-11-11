package store.common.dto;

public record PromotionConditionResult(
        int applicableSetCount,
        int stockUsed,
        int remainingQuantity,
        String message
) {
    public static PromotionConditionResult from(
            int applicableSetCount, int stockUsed, int remainingQuantity, String message
    ) {
        return new PromotionConditionResult(applicableSetCount, stockUsed, remainingQuantity, message);
    }
}
