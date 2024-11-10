package store.model.promotion;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.model.Product;
import store.model.Promotion;

class BuyNGetMFreePromotionTest {

    @ParameterizedTest
    @CsvSource(value = {"3,1,5,1", "2,1,6,2"})
    void Buy_N_Get_M_Free_프로모션에서_증정_수량을_계산한다(int n, int m, int quantity, int totalFreeQuantity) {
        // given
        PromotionStrategy promotionStrategy = new BuyNGetMFreePromotion(n, m);
        Product product = Product.of("바나나", 700, 20,
                Promotion.of("과일 할인", promotionStrategy, LocalDateTime.MIN, LocalDateTime.MAX));

        // when & then
        assertEquals(promotionStrategy.applyPromotion(product, quantity).freeQuantity(), totalFreeQuantity);
    }
}