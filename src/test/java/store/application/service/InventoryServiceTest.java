package store.application.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static store.common.constant.ErrorMessage.PRODUCT_NOT_FOUND_ERROR;
import static store.common.constant.ErrorMessage.PRODUCT_OUT_OF_STOCK;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.common.dto.PurchaseRequest;
import store.common.dto.PurchaseRequest.PurchaseProductNames;
import store.model.Product;
import store.model.ProductCatalog;
import store.model.Promotion;
import store.model.promotion.BuyNGetMFreePromotion;

class InventoryServiceTest {

    private ProductCatalog productCatalog;
    private InventoryService inventoryService;

    @BeforeEach
    void setUp() {
        productCatalog = ProductCatalog.from(
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
        inventoryService = new InventoryService(productCatalog);
    }

    @Test
    void 구매_상품이_상품_목록에_존재하지_않는_경우_예외처리한다() {
        // given
        PurchaseProductNames productNames = PurchaseProductNames.from(Set.of("바나나"));

        // when & then
        assertThatThrownBy(() -> inventoryService.validateItemsExist(productNames))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(PRODUCT_NOT_FOUND_ERROR.message());
    }

    @Test
    void 일반_재고_수량과_프로모션_재고_수량을_합산해서_구매_가능_여부를_확인한다() {
        // given
        PurchaseRequest purchaseItems = PurchaseRequest.from(Map.of("오렌지 주스", 15));

        // when & then
        assertThatCode(() -> inventoryService.checkItemsStock(purchaseItems)).doesNotThrowAnyException();
    }

    @Test
    void 구매_수량이_일반_재고_수량과_프로모션_재고_수량의_합산_범위를_초과하는_경우_예외처리한다() {
        PurchaseRequest purchaseItems = PurchaseRequest.from(Map.of("오렌지 주스", 30));

        // when & then
        assertThatThrownBy(() -> inventoryService.checkItemsStock(purchaseItems))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(PRODUCT_OUT_OF_STOCK.message());
    }
}