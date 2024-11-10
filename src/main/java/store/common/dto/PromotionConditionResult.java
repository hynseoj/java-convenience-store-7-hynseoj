package store.common.dto;

public record PromotionConditionResult(
        String message
) {
    public static PromotionConditionResult from(String message) {
        return new PromotionConditionResult(message);
    }
}
