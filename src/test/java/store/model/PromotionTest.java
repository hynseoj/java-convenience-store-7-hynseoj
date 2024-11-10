package store.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import store.model.promotion.BuyNGetMFreePromotion;

class PromotionTest {

    @Test
    void 오늘_날짜가_프로모션_기간에_해당하면_참을_반환한다() {
        // given
        Promotion promotion = Promotion.of(
                "진행 중인 프로모션",
                new BuyNGetMFreePromotion(1, 1),
                LocalDateTime.MIN,
                LocalDateTime.MAX
        );

        // when & then
        assertTrue(promotion.isWithinPromotionPeriod());
    }

    @Test
    void 오늘_날짜가_프로모션_기간에_해당하지_않으면_거짓을_반환한다() {
        // given
        Promotion promotion = Promotion.of(
                "지난 프로모션",
                new BuyNGetMFreePromotion(1, 1),
                LocalDateTime.MIN,
                LocalDateTime.of(2024, 10, 31, 23, 59)
        );

        // when & then
        assertFalse(promotion.isWithinPromotionPeriod());
    }
}