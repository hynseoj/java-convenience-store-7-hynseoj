package store.application.service;

import java.util.Set;
import java.util.stream.Collectors;
import store.common.dto.PurchaseRequest.PurchaseProductNames;
import store.model.Product;
import store.model.ProductCatalog;

public class PromotionService {

    private final ProductCatalog productCatalog;

    public PromotionService(ProductCatalog productCatalog) {
        this.productCatalog = productCatalog;
    }

    public Set<Product> getPromotionalProducts(PurchaseProductNames productNames) {
        return productCatalog.getProductsByNames(productNames.productNames())
                .stream()
                .filter(Product::isPromotionApplicable)
                .collect(Collectors.toSet());
    }
}
