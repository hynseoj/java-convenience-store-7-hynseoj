package store.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.common.dto.PurchaseRequest.PurchaseProductNames;
import store.model.Product;
import store.model.ProductCatalog;
import store.model.Products;
import store.model.Promotion;
import store.model.promotion.BuyNGetMFreePromotion;

class PromotionServiceTest {

    private ProductCatalog productCatalog;
    private PromotionService promotionService;

    @BeforeEach
    void setUp() {
        productCatalog = ProductCatalog.from(
                Set.of(
                        Product.of("오렌지 주스", 3000, 10, null),
                        Product.of("오렌지 주스", 3000, 10,
                                Promotion.of("과일드세요",
                                        new BuyNGetMFreePromotion(2, 1), LocalDate.MIN, LocalDate.MAX)),
                        Product.of("샌드위치", 2500, 10,
                                Promotion.of("아침드세요",
                                        new BuyNGetMFreePromotion(2, 1), LocalDate.MIN, LocalDate.MAX)),
                        Product.of("풍선껌", 500, 5, null)
                )
        );
        promotionService = new PromotionService(productCatalog);
    }

    @Test
    void 구매_상품_목록에서_프로모션이_적용_가능한_상품_목록을_추출한다() {
        // given
        PurchaseProductNames productNames = PurchaseProductNames.from(Set.of("오렌지 주스", "풍선껌"));

        // when
        Products promotionalProducts = promotionService.getPromotionalProducts(productNames);

        // then
        promotionalProducts.products().forEach(product -> System.out.println(product.name() + product.promotion()));
        assertThat(promotionalProducts.products()).hasSize(1);
    }
}