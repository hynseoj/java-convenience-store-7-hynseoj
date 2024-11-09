package store.model;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDateTime;
import java.util.Objects;
import store.model.promotion.PromotionStrategy;

public class Promotion {

    private final String name;
    private final PromotionStrategy promotionStrategy;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    private Promotion(
            String name, PromotionStrategy promotionStrategy, LocalDateTime startDate, LocalDateTime endDate
    ) {
        this.name = name;
        this.promotionStrategy = promotionStrategy;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static Promotion of(
            String name, PromotionStrategy promotionStrategy, LocalDateTime startDate, LocalDateTime endDate
    ) {
        return new Promotion(name, promotionStrategy, startDate, endDate);
    }

    public boolean isWithinPromotionPeriod() {
        LocalDateTime now = DateTimes.now();
        return (now.isEqual(startDate) || now.isAfter(startDate)) &&
                (now.isEqual(endDate) || now.isBefore(endDate));
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
