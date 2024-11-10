package store.application.service;

import static store.common.constant.ErrorMessage.PRODUCT_NOT_FOUND_ERROR;
import static store.common.constant.ErrorMessage.PRODUCT_OUT_OF_STOCK;

import java.util.Map;
import java.util.Set;
import store.model.Product;
import store.model.Products;

public class InventoryService {

    private final Products products;

    public InventoryService(Products products) {
        this.products = products;
    }

    public void validateItemsExist(Set<String> products) {
        boolean doesNotContainsAllProduct = !this.products.doesContainsAllProduct(products);
        if (doesNotContainsAllProduct) {
            throw new IllegalArgumentException(PRODUCT_NOT_FOUND_ERROR.message());
        }
    }

    public void checkItemsStock(Map<Set<Product>, Integer> purchaseItems) {
        purchaseItems.forEach((product, quantity) -> {
            int totalStock = product.stream().mapToInt(Product::stock).sum();
            boolean isOutOfStock = quantity > totalStock;
            if (isOutOfStock) {
                throw new IllegalArgumentException(PRODUCT_OUT_OF_STOCK.message());
            }
        });
    }
}
