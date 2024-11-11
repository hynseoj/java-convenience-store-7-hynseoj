package store.model;

import java.util.Map;

public class PromotionCatalog {

    private final Map<String, Promotion> promotions;

    private PromotionCatalog(Map<String, Promotion> promotions) {
        this.promotions = promotions;
    }

    public static PromotionCatalog from(Map<String, Promotion> promotions) {
        return new PromotionCatalog(promotions);
    }

    public Promotion getPromotionByName(String promotionName) {
        return promotions.getOrDefault(promotionName, null);
    }
}
