package store.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Promotion {

    private final String name;
    private final int purchaseQuantity;
    private final int giftQuantity;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    private Promotion(
            String name, int purchaseQuantity, int giftQuantity, LocalDateTime startDate, LocalDateTime endDate
    ) {
        this.name = name;
        this.purchaseQuantity = purchaseQuantity;
        this.giftQuantity = giftQuantity;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static Promotion of(
            String name, int purchaseQuantity, int giftQuantity, LocalDateTime startDate, LocalDateTime endDate
    ) {
        return new Promotion(name, purchaseQuantity, giftQuantity, startDate, endDate);
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
