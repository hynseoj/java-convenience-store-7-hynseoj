package store.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.model.Product;
import store.model.Products;
import store.model.Promotion;
import store.model.promotion.BuyNGetMFreePromotion;

class PromotionServiceTest {

    private Products products;
    private PromotionService promotionService;

    @BeforeEach
    void setUp() {
        products = Products.from(
                Set.of(
                        Product.of("오렌지 주스", 3000, 10, null),
                        Product.of("오렌지 주스", 3000, 10,
                                Promotion.of("과일드세요",
                                        new BuyNGetMFreePromotion(2, 1), LocalDateTime.MIN, LocalDateTime.MAX)),
                        Product.of("샌드위치", 2500, 10,
                                Promotion.of("아침드세요",
                                        new BuyNGetMFreePromotion(2, 1), LocalDateTime.MIN, LocalDateTime.MAX)),
                        Product.of("풍선껌", 500, 5, null)
                )
        );
        promotionService = new PromotionService(products);
    }

    @Test
    void 구매_상품_목록에서_프로모션이_적용_가능한_상품_목록을_추출한다() {
        // given
        Set<String> productNames = Set.of("오렌지 주스", "풍선껌");

        // when
        Set<Product> promotionalProducts = promotionService.getPromotionalProducts(productNames);

        // then
        promotionalProducts.forEach(product -> System.out.println(product.name() + product.promotion()));
        assertThat(promotionalProducts).hasSize(1);
    }
}