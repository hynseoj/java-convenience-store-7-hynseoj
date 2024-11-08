package store.view;

import java.text.NumberFormat;
import store.model.Product;
import store.model.Products;
import store.model.Promotion;

public class OutputView {

    private static final String INTRO_MESSAGE = "안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.";
    private static final String PRODUCT_MESSAGE = "- %s %s원 %s %s";
    private static final String OUT_OF_STOCK_MESSAGE = "재고 없음";

    public void printStoreInventory(Products products) {
        System.out.println(INTRO_MESSAGE);
        products.products().forEach(this::printProduct);
    }

    private void printProduct(Product product) {
        NumberFormat formatter = NumberFormat.getInstance();

        System.out.printf(
                PRODUCT_MESSAGE,
                product.name(),
                formatter.format(product.price()),
                getQuantityValue(product.quantity()),
                getPromotionNameValue(product.promotion())
        );
    }

    private String getQuantityValue(int quantity) {
        if (quantity == 0) {
            return OUT_OF_STOCK_MESSAGE;
        }
        return String.valueOf(quantity);
    }

    private String getPromotionNameValue(Promotion promotion) {
        String promotionName = "";
        if (promotion != null) {
            promotionName = promotion.name();
        }
        return promotionName;
    }
}