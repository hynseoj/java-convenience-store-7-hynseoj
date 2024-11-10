package store.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.model.promotion.BuyNGetMFreePromotion;

class ProductsTest {

    private Products products;

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
    }

    @Test
    void 상품_목록을_생성할_때_입력된_집합에_기본_상품이_없으면_추가해준다() {
        // when & then
        assertTrue(products.products().stream()
                .anyMatch(product -> product.name().equals("샌드위치") && product.promotion() == null));
    }

    @Test
    void 사용자가_입력한_상품들이_모두_상품_목록에_존재하면_참을_반환한다() {
        // given
        Set<String> productNames = Set.of("오렌지 주스", "샌드위치");

        // when & then
        assertTrue(products.doesContainsAllProduct(productNames));
    }

    @Test
    void 사용자가_입력한_상품들이_모두_상품_목록에_존재하지_않으면_거짓을_반환한다() {
        // given
        Set<String> productNames = Set.of("오렌지 주스", "초콜릿");

        // when & then
        assertFalse(products.doesContainsAllProduct(productNames));
    }

    @Test
    void 상품_이름으로_프로모션_종류에_무관한_상품_집합을_조회할_수_있다() {
        // given
        String productName = "오렌지 주스";

        // when & then
        assertThat(products.getProductByName(productName)).hasSize(2);
    }

    @Test
    void 상품_이름_목록으로_프로모션_종류에_무관한_상품_집합을_조회할_수_있다() {
        // given
        Set<String> productNames = Set.of("오렌지 주스", "샌드위치", "풍선껌");

        // when & then
        assertThat(products.getProductsByNames(productNames)).hasSize(5);
    }
}