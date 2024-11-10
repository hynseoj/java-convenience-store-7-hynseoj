package store.application.service;

import static store.common.constant.ErrorMessage.PRODUCT_NOT_FOUND_ERROR;

import java.util.Set;
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
}
