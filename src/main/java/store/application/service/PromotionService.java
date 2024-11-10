package store.application.service;

import java.util.Set;
import java.util.stream.Collectors;
import store.common.dto.PurchaseRequest.PurchaseProductNames;
import store.model.Product;
import store.model.Products;

public class PromotionService {

    private final Products products;

    public PromotionService(Products products) {
        this.products = products;
    }

    public Set<Product> getPromotionalProducts(PurchaseProductNames productNames) {
        return products.getProductsByNames(productNames.productNames())
                .stream()
                .filter(Product::isPromotionApplicable)
                .collect(Collectors.toSet());
    }
}
