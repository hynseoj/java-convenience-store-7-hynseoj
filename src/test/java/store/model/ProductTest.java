package store.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ProductTest {

    @ParameterizedTest
    @CsvSource(value = {"5,15", "3,17", "10,10"})
    void 상품_재고을_입력된_수량만큼_감소한다(int quantity, int expected) {
        // given
        Product product = Product.of("오렌지 주스", 3000, 20, null);

        // when
        product.reduceStock(quantity);

        // then
        assertEquals(expected, product.stock());
    }
}