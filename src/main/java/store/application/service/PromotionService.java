package store.application.service;

import java.util.stream.Collectors;
import store.common.dto.PurchaseRequest.PurchaseProductNames;
import store.model.Product;
import store.model.ProductCatalog;
import store.model.Products;

public class PromotionService {

    private final ProductCatalog productCatalog;

    public PromotionService(ProductCatalog productCatalog) {
        this.productCatalog = productCatalog;
    }

    public Products getPromotionalProducts(PurchaseProductNames productNames) {
        return Products.from(
                productCatalog.getProductsByNames(productNames.productNames()).products().stream()
                        .filter(Product::isPromotionApplicable)
                        .collect(Collectors.toSet())
        );
    }
}
