package store.application.service;

import store.model.Product;
import store.model.ProductCatalog;

public class InventoryService {

    private final ProductCatalog productCatalog;

    public InventoryService(ProductCatalog productCatalog) {
        this.productCatalog = productCatalog;
    }

    public void reduceStock(Product product, int quantity) {
        product.reduceStock(quantity);
    }
}
