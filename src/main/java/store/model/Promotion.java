package store.model;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.util.Objects;
import store.common.dto.PromotionConditionResult;
import store.common.dto.PromotionResult;
import store.model.promotion.PromotionStrategy;

public class Promotion {

    private final String name;
    private final PromotionStrategy promotionStrategy;
    private final LocalDate startDate;
    private final LocalDate endDate;

    private Promotion(
            String name, PromotionStrategy promotionStrategy, LocalDate startDate, LocalDate endDate
    ) {
        this.name = name;
        this.promotionStrategy = promotionStrategy;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static Promotion of(
            String name, PromotionStrategy promotionStrategy, LocalDate startDate, LocalDate endDate
    ) {
        return new Promotion(name, promotionStrategy, startDate, endDate);
    }

    public boolean isWithinPromotionPeriod() {
        LocalDate now = LocalDate.from(DateTimes.now());
        return (now.isEqual(startDate) || now.isAfter(startDate)) &&
                (now.isEqual(endDate) || now.isBefore(endDate));
    }

    public PromotionResult applyPromotion(Product product, int quantity) {
        return promotionStrategy.applyPromotion(product, quantity);
    }

    public PromotionConditionResult checkCondition(Product product, int quantity) {
        return promotionStrategy.checkCondition(product, quantity);
    }

    public String name() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Promotion promotion = (Promotion) o;
        return Objects.equals(name, promotion.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
